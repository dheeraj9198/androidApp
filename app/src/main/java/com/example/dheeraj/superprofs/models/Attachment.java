package com.example.dheeraj.superprofs.models;

/**
 * Created by dheeraj on 18/2/15.
 */
public final class Attachment {

    private int id;
    private String name;
    private String url;
    private String file_name;
    private String file_extension;
    private String description;
    private String mimetype;

    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", file_name='" + file_name + '\'' +
                ", file_extension='" + file_extension + '\'' +
                ", description='" + description + '\'' +
                ", mimetype='" + mimetype + '\'' +
                '}';
    }

    public String getCompleteFileName(){
        return file_name+"."+file_extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
    }

    public String getDescription() {
        return description;
    }

    public void setDexcription(String description) {
        this.description = description;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
