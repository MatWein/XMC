package io.github.matwein.xmc.be.repositories.category;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.Projections;
import io.github.matwein.xmc.be.common.QueryUtil;
import io.github.matwein.xmc.common.stubs.PagingParams;
import io.github.matwein.xmc.common.stubs.category.CategoryOverviewFields;
import io.github.matwein.xmc.common.stubs.category.DtoCategoryOverview;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static io.github.matwein.xmc.be.entities.QBinaryData.binaryData;
import static io.github.matwein.xmc.be.entities.cashaccount.QCategory.category;

@Repository
public class CategoryRepository {
    private final QueryUtil queryUtil;
	
	@Autowired
    public CategoryRepository(QueryUtil queryUtil) {
        this.queryUtil = queryUtil;
	}

    public QueryResults<DtoCategoryOverview> loadOverview(PagingParams<CategoryOverviewFields> pagingParams) {
        String filter = "%" + StringUtils.defaultString(pagingParams.getFilter()) + "%";

        return queryUtil.createPagedQuery(pagingParams, CategoryOverviewFields.NAME, Order.ASC)
                .select(Projections.bean(DtoCategoryOverview.class,
                        category.id, category.name,
                        binaryData.rawData.as("icon"), category.creationDate))
                .from(category)
                .leftJoin(category.icon(), binaryData)
                .where(category.deletionDate.isNull())
                .where(category.name.likeIgnoreCase(filter))
                .fetchResults();
    }
}
