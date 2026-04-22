package lt.linas_puplauskas.service.auth;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component
public class VaadinServiceInitListenerImpl implements VaadinServiceInitListener {

    private final SecurityService securityService;

    public VaadinServiceInitListenerImpl(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent ->
                uiEvent.getUI().addBeforeEnterListener(securityService)
        );
    }
}
