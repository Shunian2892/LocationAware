package com.example.location_aware.RouteRecyclerView;

import java.util.ArrayList;

class RouteManager {
    private ArrayList<Route> routeList;

    public RouteManager(){
        this.routeList = new ArrayList<>();
        init();
    }
    public void init(){
        this.routeList.add(new Route("station",new String[]{"Breda station","Breda belgieplein","Breda Hogeschool Avans"}));
        this.routeList.add(new Route("a",new String[]{"Breda station","Breda belgieplein"}));
        this.routeList.add(new Route("b",new String[]{"4","5","6"}));
        this.routeList.add(new Route("c",new String[]{"7","8","9"}));
        this.routeList.add(new Route("d",new String[]{"10","11","12"}));
        this.routeList.add(new Route("e",new String[]{"13","14","15"}));
        this.routeList.add(new Route("f",new String[]{"16","17","18"}));
    }

    public void AddRoute(Route route){
        this.routeList.add(route);
    }

    public ArrayList<Route> getRouteList(){
        return this.routeList;
    }
}
