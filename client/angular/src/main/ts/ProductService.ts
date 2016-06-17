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
              callback(products);
        }
    }
}

hf.marketdataprovider.services.service('hf.marketdataprovider.productService', hf.marketdataprovider.HalProductService);