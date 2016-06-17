/// <reference path="../../../typings/jasmine/jasmine.d.ts" />
/// <reference path="../../../typings/angularjs/angular.d.ts" />
/// <reference path="../../../typings/angularjs/angular-mocks.d.ts" />
/// <reference path="../../main/ts/MainController.ts" />
/// <reference path="../../main/ts/Product.ts" />
/// <reference path="../../main/ts/SimpleProduct.ts" />
/// <reference path="../../main/ts/ProductService.ts" />

describe('Main Controller', () => {
    var scope: hf.marketdataprovider.WebAppScope;

    beforeEach(() => {
        inject(function ($rootScope: ng.IRootScopeService) {
            scope = <hf.marketdataprovider.WebAppScope>$rootScope.$new();
        })
    });

    it('should have scope with initialized products', () => {

        var productServiceMock: hf.marketdataprovider.ProductService = {
            getAllProducts(callback:(products: hf.marketdataprovider.Product[]) => void) {
                var products: hf.marketdataprovider.Product[] = [];
                callback(products);
            }
        }

        var controller:hf.marketdataprovider.MainController = new hf.marketdataprovider.MainController(scope, productServiceMock);
        expect(controller).toBeDefined();
        expect(scope).toBeDefined();
        expect(scope.products).toBeDefined();
        expect(scope.products.length).toBe(0);
    });
});