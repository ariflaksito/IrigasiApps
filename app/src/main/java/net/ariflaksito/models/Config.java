package net.ariflaksito.models;

/**
 * Created by ariflaksito on 10/11/17.
 */

public class Config {

    private String uri = "http://192.168.8.102/irigasi-web/";
    private String urlServer;
    private String uploadUrl;

    public Config(){
        urlServer = uri + "api/";
        uploadUrl = uri + "upload.php";
    }

    public String getUrlServer() {
        return urlServer;
    }

    public void setUrlServer(String urlServer) {
        this.urlServer = urlServer;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }
}
