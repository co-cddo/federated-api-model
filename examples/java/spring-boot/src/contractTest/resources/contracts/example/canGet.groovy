package contracts.example

import org.springframework.cloud.contract.spec.Contract

Contract.make {
  request {
    method 'GET'
    url '/apis'
    headers {
      accept('application/*+json')
    }
  }
  response {
    status OK()
    headers {
      contentType('application/vnd.uk.gov.api.v1alpha+json')
    }
  }
}
