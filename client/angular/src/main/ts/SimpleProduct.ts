///<reference path="Product.ts"/>

module hf.marketdataprovider {
    export class SimpleProduct implements Product {

        /*constructor(private id:string, private version:string, private productId:string, private description:string, private imageUrl:string, private price:string) {
        }*/
        constructor(private id:string, private description:string, private price:string) {
        }

        getId(): string {
            return this.id;
        }

        /*getVersion(): string {
            return this.version;
        }

        getProductId(): string {
            return this.productId;
        }*/

        getDescription(): string {
            return this.description;
        }

        /*getImageUrl(): string {
            return this.imageUrl;
        }*/

        getPrice(): string {
            return this.price;
        }
    }
}