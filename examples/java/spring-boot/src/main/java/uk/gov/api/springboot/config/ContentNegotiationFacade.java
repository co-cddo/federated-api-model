package uk.gov.api.springboot.config;

import me.jvt.contentnegotiation.ContentTypeNegotiator;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import javax.servlet.http.HttpServletRequest;

public interface ContentNegotiationFacade {

  MediaType negotiate(HttpServletRequest request, ContentTypeNegotiator contentTypeNegotiator) throws HttpMediaTypeNotAcceptableException;

}
