module hf.marketdataprovider {
    export interface Product {
        getId(): string;
        /*getVersion(): string;
        getProductId(): string;*/
        getDescription(): string;
        /*getImageUrl(): string;*/
        getPrice(): string;
    }

    export interface ProductResource extends Product, ng.resource.IResource<Product> {
        $update(): ProductResource;
    }

    export interface ProductsResource extends ng.resource.IResourceClass<ProductResource> {
        update(Product): ProductResource;
    }
}