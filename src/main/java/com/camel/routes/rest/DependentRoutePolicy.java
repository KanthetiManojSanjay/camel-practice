package com.camel.routes.rest;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.support.RoutePolicySupport;

/**
 * @author kansanja on 25/05/25.
 */
public class DependentRoutePolicy extends RoutePolicySupport {
    private String routeName1;
    private String routeName2;

    public DependentRoutePolicy(String routeName1, String routeName2) {
        this.routeName1 = routeName1;
        this.routeName2 = routeName2;
    }

    @Override
    public void onStart(Route route) {
        CamelContext camelContext = route.getCamelContext();
        try {
            camelContext.getRouteController().startRoute(routeName1);
            camelContext.getRouteController().startRoute(routeName2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop(Route route) {
        CamelContext camelContext = route.getCamelContext();
        try {
            camelContext.getRouteController().stopRoute(routeName1);
            camelContext.getRouteController().stopRoute(routeName2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
