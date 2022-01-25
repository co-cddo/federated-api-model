package uk.gov.api.springboot.config;

import me.jvt.contentnegotiation.ContentTypeNegotiator;
import me.jvt.contentnegotiation.NotAcceptableException;
import org.springframework.http.MediaType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class ContentNegotiationFacadeImpl implements ContentNegotiationFacade {

  private ContentNegotiationManager contentNegotiationManager;

  @Override
  public MediaType negotiate(HttpServletRequest request, ContentTypeNegotiator contentTypeNegotiator) throws HttpMediaTypeNotAcceptableException {
    List<MediaType> mediaTypes =
            contentNegotiationManager.resolveMediaTypes(new DispatcherServletWebRequest(request));
    List<me.jvt.http.mediatype.MediaType> converted =
            mediaTypes.stream().map(me.jvt.http.mediatype.MediaType::from).collect(Collectors.toList());
    try {
      me.jvt.http.mediatype.MediaType negotiated = contentTypeNegotiator.negotiate(converted);
      return MediaType.valueOf(negotiated.toString());
    } catch (NotAcceptableException e) {
      throw new HttpMediaTypeNotAcceptableException(e.getMessage());
    }
  }
}
