package com.paqha.market.persistence;

import com.paqha.market.domain.Product;
import com.paqha.market.domain.repository.ProductRepository;
import com.paqha.market.persistence.crud.ProductoCrudRepository;
import com.paqha.market.persistence.entity.Producto;
import com.paqha.market.persistence.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductoRepository implements ProductRepository {

    private ProductoCrudRepository productoCrudRepository;
    private ProductMapper productMapper;

    @Autowired
    public ProductoRepository(ProductoCrudRepository productoCrudRepository, ProductMapper productMapper) {
        this.productoCrudRepository = productoCrudRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<Product> getAll() {
        List<Producto> productos= (List<Producto>) productoCrudRepository.findAll();
        return productMapper.toProducts(productos);


    }
    @Override
    public Optional<List<Product>>  getByCategory(int categoryId) {

        List<Producto> productos = productoCrudRepository.findByIdCategoriaOrderByNombreAsc(categoryId);
        return Optional.of(productMapper.toProducts(productos));
    }

    @Override
    public  Optional<List<Product>> getScarseProducts(int quantity){
        Optional<List<Producto>> productos = productoCrudRepository.findByCantidadStockLessThanAndEstado(quantity,true);
        return  productos.map((prods) ->productMapper.toProducts(prods));
    }

    @Override
    public Optional<Product> getProduct(int productId) {
        return productoCrudRepository.findById(productId).map(producto -> productMapper.toProduct(producto));

    }

    @Override
    public Product save(Product product) {
        Producto producto = productMapper.toProducto(product);
        return productMapper.toProduct(productoCrudRepository.save(producto));
    }

    @Override
    public void delete(int productId) {
        productoCrudRepository.deleteById(productId);
    }

    public Producto update(Producto _producto, int idProducto) {
        return productoCrudRepository.findById(idProducto).map(producto -> {
            producto.setCantidadStock(_producto.getCantidadStock());
            producto.setEstado(_producto.getEstado());
            producto.setNombre(_producto.getNombre());
            producto.setCodigoDeBarras(_producto.getCodigoDeBarras());
            producto.setPrecioVenta(_producto.getPrecioVenta());
            return productoCrudRepository.save(producto);
        }).get();
    }


}
