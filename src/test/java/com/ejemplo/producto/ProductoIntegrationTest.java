package com.ejemplo.producto;


import com.ejemplo.producto.Modelo.Producto;
import com.ejemplo.producto.Repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductoIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductoRepository productoRepository; // 👈 Inyectamos el repositorio real para alimentar la base de datos

    @Test
    void debeRetornarListaDeProductos() {
        // 1. PREPARAR: Insertamos un producto real en la base de datos H2 para que
        //  la lista no esté vacía
        Producto productoBase = new Producto();
        productoBase.setNombre("Teclado de Prueba");
        productoBase.setPrecio(15000.0);
        productoRepository.save(productoBase); // Guardado real

        // 2. ACTUAR: Ahora sí hacemos la petición HTTP al endpoint
        ResponseEntity<String> response = restTemplate.getForEntity("/productos", String.class);

        // 3. AFIRMAR: Verificamos que responda con éxito y que contenga los
        //  datos del producto insertado
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("id"); 
        assertThat(response.getBody()).contains("Teclado de Prueba");
    }
}
