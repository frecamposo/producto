package com.ejemplo.producto;

import com.ejemplo.producto.Controller.ProductoController;
import com.ejemplo.producto.Modelo.Producto;
import com.ejemplo.producto.Repository.ProductoRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito; // 👈 Descomentado e importado correctamente
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; // 👈 Ruta oficial corregida
import org.springframework.boot.test.mock.mockito.MockBean; // 👈 Importación oficial para Spring Boot 3.2.4
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoRepository repository; // Simula la base de datos

    @Test
    void debeCrearUnProducto() throws Exception {
        Producto prod = new Producto();
        prod.setId(1L);
        prod.setNombre("Laptop");
        prod.setPrecio(800.0);

        Mockito.when(repository.save(Mockito.any(Producto.class))).thenReturn(prod);

        mockMvc.perform(post("/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Laptop\",\"precio\":800.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop"));
    }
    // 2. NUEVA PRUEBA: LEER TODOS (GET)
    @Test
    void debeListarTodosLosProductos() throws Exception {
        // Preparamos dos productos simulados
        Producto p1 = new Producto();
        p1.setId(1L); p1.setNombre("Laptop"); p1.setPrecio(800.0);
        
        Producto p2 = new Producto();
        p2.setId(2L); p2.setNombre("Mouse"); p2.setPrecio(20.0);

        // Simulamos que el repositorio devuelve la lista con los dos productos
        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                // Verificamos el tamaño de la lista devuelta
                .andExpect(jsonPath("$.length()").value(2)) 
                // Verificamos el primer elemento
                .andExpect(jsonPath("$[0].nombre").value("Laptop"))
                // Verificamos el segundo elemento
                .andExpect(jsonPath("$[1].nombre").value("Mouse"));
    }

     // 3. NUEVA PRUEBA: ACTUALIZAR (PUT)
    @Test
    void debeActualizarUnProductoExistente() throws Exception {
        // El producto como está guardado actualmente en la base de datos simulada
        Producto productoExistente = new Producto();
        productoExistente.setId(1L);
        productoExistente.setNombre("Laptop Vieja");
        productoExistente.setPrecio(500.0);

        // El producto modificado que guardará el repositorio
        Producto productoActualizado = new Producto();
        productoActualizado.setId(1L);
        productoActualizado.setNombre("Laptop Nueva");
        productoActualizado.setPrecio(900.0);

        // Simulamos que el repositorio encuentra el producto por su ID
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(productoExistente));
        // Simulamos que el repositorio guarda los cambios con éxito
        Mockito.when(repository.save(Mockito.any(Producto.class))).thenReturn(productoActualizado);

        mockMvc.perform(put("/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Laptop Nueva\",\"precio\":900.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Laptop Nueva"))
                .andExpect(jsonPath("$.precio").value(900.0));
    }

        // 4. NUEVA PRUEBA: BORRAR (DELETE)
    @Test
    void debeBorrarUnProducto() throws Exception {
        // Como el método del repositorio devuelve "void", simulamos que no hace nada (lo borra)
        Mockito.doNothing().when(repository).deleteById(1L);

        mockMvc.perform(delete("/productos/1"))
                .andExpect(status().isOk()); // Verifica que responde un HTTP 200 exitoso

        // Verificación extra: Nos aseguramos de que el método deleteById realmente fue llamado 1 vez
        Mockito.verify(repository, Mockito.times(1)).deleteById(1L);
    } 
}

