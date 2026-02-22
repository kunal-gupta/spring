package com.visitorapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * SPA route forwarder for React UI.
 *
 * Concept:
 * - React router handles these routes in browser.
 * - Backend must return index.html for deep-link refreshes.
 *
 * Common mistake:
 * - Not forwarding frontend routes, causing 404 when user refreshes /visit or /login.
 */
@Controller
public class FrontendController {

    @GetMapping({"/login", "/register", "/visit", "/visit/**"})
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
