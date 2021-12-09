package org.example.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "microservice-paid",
        url = "${microservice-paid.url}",
        path = "${microservice-paid.api-base-url}"
)
public interface PaymentClient {
    @GetMapping("/isPaid/{idProject}")
    ResponseEntity<Boolean> isPaid(@PathVariable(name = "idProject") Long idProject);
}
