package xiaolei.repluginmain.db.model;

import org.litepal.crud.DataSupport;

/**
 * Created by sunxl8 on 2017/8/2.
 */

public class PluginModel extends DataSupport {

    private int id;

    private String pkgName;

    private String icUrl;

    private int icRes;

    private String name;

    private int order;

    public int getIcRes() {
        return icRes;
    }

    public void setIcRes(int icRes) {
        this.icRes = icRes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getIcUrl() {
        return icUrl;
    }

    public void setIcUrl(String icUrl) {
        this.icUrl = icUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
