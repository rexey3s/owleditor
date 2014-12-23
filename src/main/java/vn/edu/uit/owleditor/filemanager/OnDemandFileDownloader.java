package vn.edu.uit.owleditor.filemanager;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;

import java.io.IOException;

/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecomunication created on 12/14/14.
 */
public class OnDemandFileDownloader extends FileDownloader {


    private static final long serialVersionUID = 1L;
    private final OnDemandStreamResource onDemandStreamResource;

    public OnDemandFileDownloader(OnDemandStreamResource onDemandStreamResource) {
        super(new StreamResource(onDemandStreamResource, ""));
        this.onDemandStreamResource = checkNotNull(onDemandStreamResource,
                "The given on-demand stream resource may never be null!");
    }

    @Override
    public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path)
            throws IOException {
        getResource().setFilename(onDemandStreamResource.getFilename());
        return super.handleConnectorRequest(request, response, path);
    }

    private StreamResource getResource() {
        return (StreamResource) this.getResource("dl");
    }

    private OnDemandStreamResource checkNotNull(OnDemandStreamResource o, String message) {
        if (o == null) {
            throw new NullPointerException(message);
        } else return o;
    }

    public interface OnDemandStreamResource extends StreamResource.StreamSource {
        String getFilename();
    }

}