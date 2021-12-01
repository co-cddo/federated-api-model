package uk.gov.api.springboot.repositories;

import java.util.List;
import uk.gov.api.springboot.daos.MetadataDao;

public interface MetadataRepository {

  List<MetadataDao> findAll();
}
