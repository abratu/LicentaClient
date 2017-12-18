package licentaclient.abratu.com.licentaclient.entity;

import java.util.ArrayList;
import java.util.List;

import licentaclient.abratu.com.licentaclient.transformers.JsonTransformer;
import licentaclient.abratu.com.licentaclient.utils.StringUtils;

/**
 * Created by apetho on 12/18/2017.
 */

public class Response {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    private String status;
    private String data;
    private String detailedMessage;

    public Response(String status, String data, String detailedMessage) {
        this.status = status;
        this.data = data;
        this.detailedMessage = detailedMessage;
    }

    public Response() {
    }

    public static Response mindResponse(String message) {
        return new JsonTransformer<Response>().transform2Object(message, Response.class);
    }

    public static <T> List<T> getResponseList(String message, Class<T> clazz) {
        if (StringUtils.isEmpty(message)) {
            return null;
        }
        Response reponse = mindResponse(message);
        if (reponse.isSuccess()) {
            return reponse.getDataList(clazz);
        } else {
            return new ArrayList<>();
        }
    }

    public static <T> T getResponseData(String message, Class<T> clazz) {
        if (StringUtils.isEmpty(message)) {
            return null;
        }
        Response reponse = mindResponse(message);
        if (reponse.isSuccess()) {
            return reponse.getDataObject(clazz);
        } else {
            return null;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    public boolean isSuccess() {
        return status.equalsIgnoreCase(SUCCESS);
    }

    private <T> T getDataObject(Class<T> clazz) {
        return new JsonTransformer<T>().transform2Object(this.data, clazz);
    }

    private <T> List<T> getDataList(Class<T> clazz) {
        return new JsonTransformer<T>().transform2List(data, clazz);
    }
}
