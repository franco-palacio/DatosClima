class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/datos/index", controller: "datos", action: "index")
        "500"(view:'/error')

        "/datos/clima/$dia"(controller: "datos", action: "getClima")

        "/datos/periodos"(controller: "datos", action: "getPeriodos")
	}
}
