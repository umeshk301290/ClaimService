package com.prgx.workbench.claim.document.metadata;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.prgx.workbench.claim.proxy.HttpServletRequestHeaderProxy;
import com.prgx.workbench.core.data.dictionary.config.UUIDIdConverter;
import com.prgx.workbench.core.odata.entity.provider.EntityProvider;
import com.prgx.workbench.core.odata.expression.impl.BaseExpression;
import com.prgx.workbench.core.odata.expression.impl.BinaryExpression;
import com.prgx.workbench.core.odata.expression.impl.FieldNameExpression;
import com.prgx.workbench.core.odata.expression.impl.LiteralValueExpression;
import com.prgx.workbench.core.odata.expression.impl.operator.BinaryOperator;
import com.prgx.workbench.core.odata.model.Query;
import com.prgx.workbench.core.odata.rest.impl.OdataRestRequestHandler;
import com.prgx.workbench.odata.jpa.mapper.Merger;
import com.prgx.workbench.odata.jpa.service.impl.BaseDeletableService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DocumentMetaDataService extends BaseDeletableService<DocumentMetaData, UUID> {

    @Autowired
    private DocumentMetaDataEntityProvider entityProvider;

    @Autowired
    private DocumentMetaDataRepository repository;

    @Autowired
    private DocumentMetaDataMerger merger;

    @Autowired
    private UUIDIdConverter idConverter;

    @Autowired
    private DocumentMetaDataConverter converter;
    
    private OdataRestRequestHandler odataRestRequestHandler;
    
    private static final String CODE = "code";

    @Override
    public UUIDIdConverter getIdMapper() {
        return idConverter;
    }

    @Override
    public EntityProvider getEntityProvider() {
        return entityProvider;
    }

    @Override
    public Merger<DocumentMetaData> getMerger() {
        return merger;
    }

    @Override
    public DocumentMetaDataConverter getEntityMapper() {
        return converter;
    }

    @Override
    public Class<DocumentMetaData> getEntityClass() {
        return DocumentMetaData.class;
    }

    @Override
    public DocumentMetaDataRepository getRepository() {
        return repository;
    }

    @Override
    public DocumentMetaData save(DocumentMetaData entity) {
        return getRepository().save(entity);
    }
    
    public void getObjectStoreMetaDataByCode(HttpServletRequest request, HttpServletResponse response, String code){
        try {
            request.setAttribute("requestMapping","metadata");
            Map<String, String> headers = Collections
                    .list(request.getHeaderNames())
                    .stream()
                    .collect(Collectors.toMap(Function.identity(), request::getHeader));
            headers.put(CODE, code);
            odataRestRequestHandler.handle(HttpServletRequestHeaderProxy.proxy(request, headers), response);
        } catch (IOException e) {
            log.error("Exception while fetching metadata", e);
        }
    }


    public Collection<DocumentMetaData> getAllEntity(Query query) {
        addCodeFilter(query);
        return super.getAllEntity(query);
    }

    public long count(Query query) {
        addCodeFilter(query);
        return super.count(query);
    }

    public List<Object[]> groupBy(Query query) {
        addCodeFilter(query);
        return super.groupBy(query);
    }

    public Map.Entry<Long, Long> findIndexes(Query query) {
        addCodeFilter(query);
        return super.findIndexes(query);
    }

    public List<Object[]> getAllWithFindOption(Query query) {
        addCodeFilter(query);
        return super.getAllWithFindOption(query);
    }

    public List<Object[]> getAllWithSelectOption(Query query) {
        addCodeFilter(query);
        return super.getAllWithSelectOption(query);
    }

    private void addCodeFilter(Query query) {
        Map<String, List<String>> headers = query.getHeaders();
        if(Objects.nonNull(headers) && Objects.nonNull(headers.get(CODE))) {
            FieldNameExpression idFieldExpression = FieldNameExpression.builder()
                    .field(CODE).build();
            LiteralValueExpression idExpressionValue = LiteralValueExpression.builder().value(headers.get(CODE).get(0)).build();
            BinaryExpression exp = BinaryExpression.builder().left(idFieldExpression).operator(BinaryOperator.EQ).right(idExpressionValue).build();
            if (Objects.nonNull(query.getFilter()))
                query.setFilter(BinaryExpression.builder().left(exp).operator(BinaryOperator.AND)
                        .right((BaseExpression) query.getFilter()).build());
            else
                query.setFilter(exp);
        }
    }

    @Autowired
    @Lazy
    public void setOdataRestRequestHandler(OdataRestRequestHandler odataRestRequestHandler) {
        this.odataRestRequestHandler = odataRestRequestHandler;
    }

    public List<DocumentMetaData> findByDocumentIds(List<UUID> documentIds) {
        return repository.findByDocumentId(documentIds);
    }
}
