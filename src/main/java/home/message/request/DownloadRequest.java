package home.message.request;

public class DownloadRequest {
    /*
        DownloadRequest
        ===============
        Request to download a file
        Connection : CommunityConnection
     */

    private String fileName;

    public DownloadRequest() {
    }

    public DownloadRequest(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "DOWNLOAD;" + fileName;
    }

}
