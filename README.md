# Restaurant Service

## Contributors.

1. M. Azka Yasin (05111540000038)
2. Julian Enggarrio P. P. (05111540000082)
3. Fajar Maulana F. (05111540000171)

## End Point


**Restaurant**
---
| Method | End Point | Parameter | Privilege | Description |
| ------------- | ------------- | ------------- | ------------- | ------------- |
|GET| /restaurant/lists | - | User | Lists all restaurants. |
|POST| /restaurant/deactive/{id} | - | Admin | Remove  restaurant verification. |
|POST| /restaurant/data | name, alamat, deskripsi, jam_buka, jam_tutup, kategori | Restaurant| Register restaurant data. |
|POST| /restaurant/open | - | Restaurant| Open restaurant. |
|POST| /restaurant/deal | deal | Restaurant | Deals added by restaurant. |
|POST| /restaurant/close | - | Restaurant| Close restaurant. |
|PUT| /restaurant/updatedata | name, alamat, deskripsi, jam_buka, jam_tutup, kategori | Restaurant| Update restaurant data. |

**Menu**
---
| Method | End Point | Parameter | Privilege | Description |
| ------------- | ------------- | ------------- | ------------- | ------------- |
|POST| /restaurant/menu/add | name, harga, kategori, deskripsi | Restauran | Add  restaurant Menu. |
|POST| /restaurant/menu/delete/{id} |- | Restauran | Delete  restaurant Menu. |
|POST| /restaurant/menu/outofstock/{id} |- | Restauran | No Stock  restaurant Menu. |
|PUT| /restaurant/menu/update | name, harga, kategori, deskripsi | Restauran | Update  restaurant Menu. |
|GET| /restaurant/menu/allmenu |- | Restauran | Restaurant get all Menu. |
|GET| /restaurant/menu/{id} | - | User | Get all restaurant menu. |




