package uk.gov.api.springboot.infrastructure;

import java.util.List;
import uk.gov.api.springboot.domain.models.MetadataDao;

public
interface MetadataRepository { // implements AggregateRoot<MetadataDao, MetadataDao.MetadataId> {

  List<MetadataDao> findAll();
}
