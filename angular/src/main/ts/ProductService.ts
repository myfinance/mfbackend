/// <reference path="Application.ts" />
/// <reference path="../../../typings/angularjs/angular.d.ts" />
/// <reference path="Product.ts" />

module hf.marketdataprovider {
    export interface ProductService {
        getAllProducts(callback:(products:hf.marketdataprovider.Product[]) => void);
    }

    export class HalProductService implements ProductService {
        static $inject = ['hf.marketdataprovider.apiResource', '$resource'];
        private productsContract = 'products';

        constructor(private apiResource:ng.resource.IResourceClass<ng.resource.IResource<any>>,
                    private $resource:ng.resource.IResourceService) {
            console.log('Product service started')
        }

        getAllProducts(callback:(products:hf.marketdataprovider.Product[]) => void) {
           var products:Product[] = [];
           products = <hf.marketdataprovider.ProductResource[]>this.apiResource.query(function() {
                console.log(products);
              });
              /*var data = json[0];
              console.log("test");
              console.log(data.id);
              var product:hf.marketdataprovider.Product=new SimpleProduct(json[0][0], json[0][1], json[0][2]);
              products.push(product)
              console.log(product);*/
              callback(products);
            /*this.apiResource.get((api:any) => {
                var productsResource:hf.marketdataprovider.ProductsResource = this.getProductsResource(api);
                if (productsResource) {
                    productsResource.get((result:any) => {
                        var products:hf.marketdataprovider.Product[] = [];
                        if (result.hasOwnProperty("_embedded") && result._embedded.hasOwnProperty(this.productsContract)) {
                            products = result._embedded['products'];
                        }
                        callback(products);
                    });
                }
            });*/
        }

        /*private getProductsResource(apiResource:any):hf.marketdataprovider.ProductsResource {
            if (apiResource.hasOwnProperty("_links") && apiResource._links.hasOwnProperty(this.productsContract)) {
                var href:string = apiResource._links[this.productsContract].href;
                // remove template parameters
                href = href.replace(/{.*}/g, '');

                var productsResource:hf.marketdataprovider.ProductsResource =
                    <hf.marketdataprovider.ProductsResource> this.$resource(href + '/:id', {id: '@id'}, {});
                return productsResource;
            }
        }*/
    }
}

hf.marketdataprovider.services.service('hf.marketdataprovider.productService', hf.marketdataprovider.HalProductService);