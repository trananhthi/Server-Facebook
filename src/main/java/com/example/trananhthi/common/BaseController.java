package com.example.trananhthi.common;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api")
public abstract class BaseController {

        protected final String V1 = "v1";
        protected final String V2 = "v2";
}
