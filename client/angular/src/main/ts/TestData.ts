///<reference path="Product.ts"/>
///<reference path="SimpleProduct.ts"/>

module hf.marketdataprovider {
    export function createTestProducts():Product[] {
        var products:Product[] = [];
        var testProduct1:Product = new SimpleProduct("11", "bla", "100,99");
        products.push(testProduct1)
        return products;
    }
}