package com.uj.myapplications.pojo.MenuPojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuDataPojo {

@SerializedName("menus")
@Expose
private List<MenuDetailPojo> menuDetailPojos = null;
@SerializedName("total")
@Expose
private Integer total;

public List<MenuDetailPojo> getMenuDetailPojos() {
return menuDetailPojos;
}

public void setMenuDetailPojos(List<MenuDetailPojo> menuDetailPojos) {
this.menuDetailPojos = menuDetailPojos;
}

public Integer getTotal() {
return total;
}

public void setTotal(Integer total) {
this.total = total;
}

}