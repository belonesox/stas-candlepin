/**
 * Copyright (c) 2009 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package org.fedoraproject.candlepin.model;

import org.fedoraproject.candlepin.model.Statistic.EntryType;
import org.fedoraproject.candlepin.model.Statistic.ValueType;

import com.google.inject.Inject;
import com.wideplay.warp.persist.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * StatisticCurator
 */
public class StatisticCurator extends AbstractHibernateCurator<Statistic> {

    private OwnerCurator ownerCurator;
    private OwnerInfoCurator ownerInfoCurator;

    @Inject
    public StatisticCurator(OwnerCurator ownerCurator,
        OwnerInfoCurator ownerInfoCurator) {
        super(Statistic.class);
        this.ownerCurator = ownerCurator;
        this.ownerInfoCurator = ownerInfoCurator;
    }

    @Transactional
    public Statistic create(Statistic s) {
        return super.create(s);
    }

    @SuppressWarnings("unchecked")
    public List<Statistic> getStatisticsByOwner(Owner owner, String qType,
        String reference, String vType, Date from, Date to) {
        Criteria c = currentSession().createCriteria(Statistic.class);
        c.add(Restrictions.eq("ownerId", owner.getId()));
        if (qType != null && !qType.trim().equals("")) {
            if (qType.equals("TOTALCONSUMERS")) {
                c.add(Restrictions.eq("entryType", EntryType.TOTALCONSUMERS));
            }
            else if (qType.equals("CONSUMERSBYSOCKETCOUNT")) {
                c.add(Restrictions.eq("entryType",
                    EntryType.CONSUMERSBYSOCKETCOUNT));
            }
            else if (qType.equals("TOTALSUBSCRIPTIONCOUNT")) {
                c.add(Restrictions.eq("entryType",
                    EntryType.TOTALSUBSCRIPTIONCOUNT));
            }
            else if (qType.equals("TOTALSUBSCRIPTIONCONSUMED")) {
                c.add(Restrictions.eq("entryType",
                    EntryType.TOTALSUBSCRIPTIONCONSUMED));
            }
            else if (qType.equals("PERPRODUCT")) {
                c.add(Restrictions.eq("entryType", EntryType.PERPRODUCT));
            }
            else if (qType.equals("PERPOOL")) {
                c.add(Restrictions.eq("entryType", EntryType.PERPOOL));
            }
            else if (qType.equals("SYSTEM")) {
                c.add(Restrictions.eq("entryType", EntryType.SYSTEM));
            }
            else {
                // no match, no filter
            }
        }
        if (vType != null && !vType.trim().equals("")) {
            if (vType.equals("RAW")) {
                c.add(Restrictions.eq("valueType", ValueType.RAW));
            }
            else if (vType.equals("USED")) {
                c.add(Restrictions.eq("valueType", ValueType.USED));
            }
            else if (vType.equals("CONSUMED")) {
                c.add(Restrictions.eq("valueType", ValueType.CONSUMED));
            }
            else if (vType.equals("PERCENTAGECONSUMED")) {
                c.add(Restrictions
                    .eq("valueType", ValueType.PERCENTAGECONSUMED));
            }
            else if (vType.equals("PHYSICAL")) {
                c.add(Restrictions.eq("valueType", ValueType.PHYSICAL));
            }
            else if (vType.equals("VIRTUAL")) {
                c.add(Restrictions.eq("valueType", ValueType.VIRTUAL));
            }
            else {
                // no match, no filter
            }
        }
        if (reference != null && !reference.trim().equals("")) {
            c.add(Restrictions.eq("valueReference", reference));
        }
        if (from != null) {
            c.add(Restrictions.ge("created", from));
        }
        if (to != null) {
            c.add(Restrictions.le("created", to));
        }
        return (List<Statistic>) c.list();

    }

    @SuppressWarnings("unchecked")
    public List<Statistic> getStatisticsByPool(String poolId, String vType,
        Date from, Date to) {
        Criteria c = currentSession().createCriteria(Statistic.class);
        c.add(Restrictions.eq("valueReference", poolId));
        c.add(Restrictions.eq("entryType", EntryType.PERPOOL));

        if (vType != null && !vType.trim().equals("")) {
            if (vType.equals("RAW")) {
                c.add(Restrictions.eq("valueType", ValueType.RAW));
            }
            else if (vType.equals("USED")) {
                c.add(Restrictions.eq("valueType", ValueType.USED));
            }
            else if (vType.equals("CONSUMED")) {
                c.add(Restrictions.eq("valueType", ValueType.CONSUMED));
            }
            else if (vType.equals("PERCENTAGECONSUMED")) {
                c.add(Restrictions
                    .eq("valueType", ValueType.PERCENTAGECONSUMED));
            }
            else {
                // no match, no filter
            }
        }
        if (from != null) {
            c.add(Restrictions.ge("created", from));
        }
        if (to != null) {
            c.add(Restrictions.le("created", to));
        }
        return (List<Statistic>) c.list();

    }

    @SuppressWarnings("unchecked")
    public List<Statistic> getStatisticsByProduct(String prodId, String vType,
        Date from, Date to) {
        Criteria c = currentSession().createCriteria(Statistic.class);
        c.add(Restrictions.eq("entryType", EntryType.PERPRODUCT));

        c.add(Restrictions.eq("valueReference", prodId));
        if (vType != null && !vType.trim().equals("")) {
            if (vType.equals("RAW")) {
                c.add(Restrictions.eq("valueType", ValueType.RAW));
            }
            else if (vType.equals("USED")) {
                c.add(Restrictions.eq("valueType", ValueType.USED));
            }
            else if (vType.equals("CONSUMED")) {
                c.add(Restrictions.eq("valueType", ValueType.CONSUMED));
            }
            else if (vType.equals("PERCENTAGECONSUMED")) {
                c.add(Restrictions
                    .eq("valueType", ValueType.PERCENTAGECONSUMED));
            }
            else {
                // no match, no filter
            }
        }
        if (from != null) {
            c.add(Restrictions.ge("created", from));
        }
        if (to != null) {
            c.add(Restrictions.le("created", to));
        }
        return (List<Statistic>) c.list();

    }

    public void executeStatisticRun() {
        List<Owner> owners = ownerCurator.listAll();
        for (Owner owner : owners) {
            String ownerId = owner.getId();

            systemCounts(owner);
            totalConsumers(owner);
            consumersPerSocketCount(owner);
            int tsc = totalSubscriptionCount(ownerId);
            totalSubscriptionConsumed(ownerId, tsc);
            perPool(ownerId);
            perProduct(ownerId);
        }
    }

    private void systemCounts(Owner owner) {
        OwnerInfo oi = ownerInfoCurator.lookupByOwner(owner);

        Statistic consumerCountStatistic = new Statistic(EntryType.SYSTEM,
            ValueType.VIRTUAL, null, oi.getConsumerGuestCounts().get(OwnerInfo.GUEST),
            owner.getId());
        create(consumerCountStatistic);

        consumerCountStatistic = new Statistic(EntryType.SYSTEM,
            ValueType.PHYSICAL, null, oi.getConsumerGuestCounts().get(OwnerInfo.PHYSICAL), 
            owner.getId());
        create(consumerCountStatistic);

    }

    private void totalConsumers(Owner owner) {
        int count = owner.getConsumers().size();
        Statistic consumerCountStatistic = new Statistic(
            EntryType.TOTALCONSUMERS, ValueType.RAW, null, count, owner.getId());
        create(consumerCountStatistic);
    }

    private void consumersPerSocketCount(Owner owner) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (Consumer c : owner.getConsumers()) {
            String sockets = c.getFact("cpu.cpu_socket(s)");
            Integer count = map.get(sockets);
            if (count == null) {
                count = new Integer(0);
            }
            count++;
            map.put(sockets, count);
        }
        for (String sockets : map.keySet()) {
            Statistic socketCountStatistic = new Statistic(
                EntryType.CONSUMERSBYSOCKETCOUNT, ValueType.RAW, sockets,
                map.get(sockets), owner.getId());
            create(socketCountStatistic);
        }
    }

    private int totalSubscriptionCount(String ownerId) {
        int subscriptionCountTotal = 0;
        String subscriptionCountString = "select sum(quantity) from Pool p" +
            " where p.owner.id = :ownerId";
        Query subscriptionCountQuery = currentSession().createQuery(
            subscriptionCountString).setString("ownerId", ownerId);
        Long subCount = (Long) subscriptionCountQuery.iterate().next();
        subscriptionCountTotal = (subCount == null ? 0 : subCount.intValue());
        Statistic subscriptionCountStatistic = new Statistic(
            EntryType.TOTALSUBSCRIPTIONCOUNT, ValueType.RAW, null,
            subscriptionCountTotal, ownerId);
        create(subscriptionCountStatistic);
        return subscriptionCountTotal;
    }

    private void totalSubscriptionConsumed(String ownerId,
        int subscriptionCountTotal) {

        // Total Subscription Consumed (Raw Count and Percentage)
        int entitlementCountTotal = 0;
        String entitlementCountString = "select sum(quantity) from Entitlement e" +
            " where e.owner.id = :ownerId";
        Query entitlementCountQuery = currentSession().createQuery(
            entitlementCountString).setString("ownerId", ownerId);
        Long entCount = (Long) entitlementCountQuery.iterate().next();
        entitlementCountTotal = (entCount == null ? 0 : entCount.intValue());
        Statistic entitlementCountRawStatistic = new Statistic(
            EntryType.TOTALSUBSCRIPTIONCONSUMED, ValueType.RAW, null,
            entitlementCountTotal, ownerId);
        create(entitlementCountRawStatistic);
        int percentage = 0;
        if (subscriptionCountTotal > 0) {
            percentage = (entitlementCountTotal * 100 / subscriptionCountTotal);
        }
        Statistic entitlementCountPercentageStatistic = new Statistic(
            EntryType.TOTALSUBSCRIPTIONCONSUMED, ValueType.PERCENTAGECONSUMED,
            null, percentage, ownerId);
        create(entitlementCountPercentageStatistic);
    }

    private void perPool(String ownerId) {
        String poolString = "select id from Pool p" +
            " where p.owner.id = :ownerId";
        Query poolQuery = currentSession().createQuery(poolString).setString(
            "ownerId", ownerId);
        Iterator poolIter = poolQuery.iterate();
        String perPoolString = "select count(e) from Event e " +
            "where e.target = 'ENTITLEMENT' and e.referenceType = 'POOL' " +
            "and e.type = :type and e.referenceId = :referenceId";
        while (poolIter.hasNext()) {
            String poolId = (String) poolIter.next();

            Query perPoolQuery = currentSession().createQuery(perPoolString);
            perPoolQuery.setString("type", "CREATED");
            perPoolQuery.setString("referenceId", poolId);
            Long ppUCount = (Long) perPoolQuery.iterate().next();
            int perPoolUsedCount = (ppUCount == null ? 0 : ppUCount.intValue());

            perPoolQuery.setString("type", "DELETED");
            Long ppDCount = (Long) perPoolQuery.iterate().next();
            int perPoolDeletedCount = (ppDCount == null ? 0 : ppDCount
                .intValue());
            int perPoolConsumedCount = perPoolUsedCount - perPoolDeletedCount;

            String totalPoolCountString = "select quantity from Pool p" +
                " where p.id = :poolId";
            Query totalPoolCountQuery = currentSession().createQuery(
                totalPoolCountString).setString("poolId", poolId);

            Long tpCount = (Long) totalPoolCountQuery.iterate().next();
            int totalPoolCountTotal = (tpCount == null ? 0 : tpCount.intValue());
            int poolPercentage = 0;
            if (totalPoolCountTotal > 0) {
                poolPercentage = (perPoolConsumedCount * 100 / totalPoolCountTotal);
            }
            Statistic perPoolCountPercentageStatistic = new Statistic(
                EntryType.PERPOOL, ValueType.PERCENTAGECONSUMED, poolId,
                poolPercentage, ownerId);
            create(perPoolCountPercentageStatistic);
            Statistic perPoolCountUsedStatistic = new Statistic(
                EntryType.PERPOOL, ValueType.USED, poolId, perPoolUsedCount,
                ownerId);
            create(perPoolCountUsedStatistic);
            Statistic perPoolCountConsumedStatistic = new Statistic(
                EntryType.PERPOOL, ValueType.CONSUMED, poolId,
                perPoolConsumedCount, ownerId);
            create(perPoolCountConsumedStatistic);
        }
    }

    private void perProduct(String ownerId) {
        String productString = "select distinct p.productName, p.productId from Pool p" +
            " where p.owner.id = :ownerId";
        Query productQuery = currentSession().createQuery(productString)
            .setString("ownerId", ownerId);
        Iterator productIter = productQuery.iterate();
        String perProductString = "select count(e) from Event e " +
            "where e.target = 'ENTITLEMENT' and e.referenceType = 'POOL' " +
            "and e.type = :type and e.targetName = :productName";
        while (productIter.hasNext()) {
            Object[] productName = (Object[]) productIter.next();

            Query perProductQuery = currentSession().createQuery(
                perProductString);
            perProductQuery.setString("type", "CREATED");
            perProductQuery.setString("productName", (String) productName[0]);
            Long ppUCount = (Long) perProductQuery.iterate().next();
            int perProductUsedCount = (ppUCount == null ? 0 : ppUCount
                .intValue());

            perProductQuery.setString("type", "DELETED");
            Long ppDCount = (Long) perProductQuery.iterate().next();
            int perProductDeletedCount = (ppDCount == null ? 0 : ppDCount
                .intValue());
            int perProductConsumedCount = perProductUsedCount -
                perProductDeletedCount;

            String totalProductCountString = "select sum(quantity) from Pool p" +
                " where p.productName = :productName";
            Query totalProductCountQuery = currentSession().createQuery(
                totalProductCountString).setString("productName",
                (String) productName[0]);
            Long tpCount = (Long) totalProductCountQuery.iterate().next();
            int totalProductCountTotal = (tpCount == null ? 0 : tpCount
                .intValue());
            int productPercentage = 0;
            if (totalProductCountTotal > 0) {
                productPercentage = (perProductConsumedCount * 100 / 
                                     totalProductCountTotal);
            }
            Statistic perPoolCountPercentageStatistic = new Statistic(
                EntryType.PERPRODUCT, ValueType.PERCENTAGECONSUMED,
                (String) productName[1], productPercentage, ownerId);
            create(perPoolCountPercentageStatistic);
            Statistic perPoolCountUsedStatistic = new Statistic(
                EntryType.PERPRODUCT, ValueType.USED, (String) productName[1],
                perProductUsedCount, ownerId);
            create(perPoolCountUsedStatistic);
            Statistic perPoolCountConsumedStatistic = new Statistic(
                EntryType.PERPRODUCT, ValueType.CONSUMED,
                (String) productName[1], perProductConsumedCount, ownerId);
            create(perPoolCountConsumedStatistic);
        }
    }

    protected Session currentSession() {
        Session sess = (Session) entityManager.get().getDelegate();
        return sess;
    }
}