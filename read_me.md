### POST /product

```url
http://localhost:3500/product
```

#### body

```
// all attributes are required
{
    "productId":"123455",
    "productPrice":100,
    "productName": "Sanitizer",
    "sellerName":"John"
}
```

#### Response

```
{
    "productId":"123455",
    "productPrice":100,
    "productName": "Sanitizer",
    "seller":{
        "sellerName":"John"
    }
}
```

### Get /product

```url
http://localhost:3500/product
```

#### Output

```
[
    {
    "productId":"123455",
    "productPrice":100,
    "productName": "Sanitizer",
    "seller":{
        "sellerName":"John"
        }
    },
    {
    "productId":"1235655",
    "productPrice":1000,
    "productName": "Soda",
    "seller":{
        "sellerName":"John"
        }
    }
]
```

### PUT /product/12345

```url
http://localhost:3500/product/{productId}
e.g. http://localhost:3500/product/12345
```

#### body

```
// productPrice, productName and sellerName are not required.
{
    "productPrice":100,
    "productName": "Sanitizer",
    "sellerName":"John"
}
```

#### Response

```
{
    "productId":"12345",
    "productPrice":100,
    "productName": "Sanitizer",
    "seller":{
        "sellerName":"John"
    }
}
```

### Delete /product/12345

```url
http://localhost:3500/product/{productId}
e.g. http://localhost:3500/product/12345
```

#### Response

```
Deleted product successfully
```

### POST /seller

```
http://localhost:3500/seller
```

#### body

```
// sellerName is required
{
    "sellerName":"John"
}
```

#### Response

```
{
    "sellerName": "John"
}
```

### Get /seller

```
http://localhost:3500/seller
```

#### Response

```
[
    {
        "sellerName": "John"
    }
]
```
