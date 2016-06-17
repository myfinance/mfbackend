/// <reference path="../../../typings/angularjs/angular.d.ts" />
/// <reference path="WebAppScope.ts"/>
/// <reference path="Application.ts"/>
/// <reference path="Product.ts"/>
/// <reference path="ProductService.ts"/>

module hf.marketdataprovider {
    export class MainController {
       static $inject = ['$scope', 'hf.marketdataprovider.productService'];

        constructor(private $scope:WebAppScope, private productService:ProductService) {
            this.refreshProducts();

            $scope.refreshProducts = () => {
                this.refreshProducts();
            }
        }

        private refreshProducts() {
              this.productService.getAllProducts(
                  (products:hf.marketdataprovider.Product[]) => {
                      this.$scope.products = products;
                  }
              );

        }
    }
}

hf.marketdataprovider.controllers.controller('hf.marketdataprovider.mainController', hf.marketdataprovider.MainController);