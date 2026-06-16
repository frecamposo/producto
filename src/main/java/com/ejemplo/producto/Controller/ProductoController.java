package com.ejemplo.producto.Controller;

import com.ejemplo.producto.Modelo.Producto;
import com.ejemplo.producto.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository repository;

    @GetMapping // LEER TODOS
    public List<Producto> listar() {
        return repository.findAll();
    }

    @PostMapping // CREAR
    public Producto crear(@RequestBody Producto producto) {
        return repository.save(producto);
    }

    @PutMapping("/{id}") // ACTUALIZAR
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto nuevo) {
        return repository.findById(id).map(p -> {
            p.setNombre(nuevo.getNombre());
            p.setPrecio(nuevo.getPrecio());
            return repository.save(p);
        }).orElseThrow();
    }

    @DeleteMapping("/{id}") // BORRAR
    public void borrar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}

