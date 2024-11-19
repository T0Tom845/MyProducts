# API Документация “РомашкаКо”
## Управление товарами
### Базовый URL:
`http://localhost:8080/products`
### 1. Получить список товаров
Метод: `GET`  
URL: `/products/`  
**Параметры запроса:**

| Параметр         | Тип     | Описание                                               |
|------------------|---------|--------------------------------------------------------|
| name             | String  | Фильтр по имени или части имени товара.                |
| priceGreaterThan | Double  | Фильтр по цене, товары дороже указанного значения      |
| priceLessThan    | Double  | Фильтр по цене, товары дешевле указанного значения.    |
| available        | Boolean | Фильтр по наличию товара (true для доступных товаров). |
| sortBy           | String  | Поле для сортировки (name или price).                  |
| sortOrder        | String  | Направление сортировки (asc или desc).                 |
| limit            | Integer | Количество записей на странице.                        |

#### Пример запроса:
`GET /products?name=example&priceGreaterThan=50&sortBy=name&sortOrder=asc&limit=10`

### 2. Получить товар по ID  
Метод: `GET`  
URL: `/products/{productId}`  
**Описание**: Возвращает информацию о товаре по его ID.  
**Пример запроса:**  
`GET /products/1`

### 3. Создать товар  
Метод: `POST`  
URL: `/products/`  
**Описание**: Создает новый товар.
**Тело запроса (JSON):**
``
{
  "name": "Товар",
  "description": "Описание товара",
  "price": 100.5,
  "available": true
}
``

### 4. Обновить товар  
Метод: `PATCH`  
URL: `/products/{productId}`  
**Описание**: Обновляет информацию о товаре.
**Пример запроса:**  
`GET /products/1`  
**Тело запроса (JSON):**
``
{
  "name": "Обновленный товар",
  "description": "Новое описание",
  "price": 150.0,
  "available": false
}
``


### 5. Удалить товар  
Метод: `DELETE`  
URL: `/products/{productId}`  
**Описание**: Удаляет товар по указанному ID.
**Пример запроса:**  
`DELETE /products/1`  

# Управление продажами

### Базовый URL:
`http://localhost:8080/sales/`

## **1. Получить список всех продаж**

**Метод:** `GET`  
**URL:** `/sales/`  

**Описание:** Возвращает список всех продаж.

### Пример запроса:
`GET /sales/`

## **2. Получить продажу по ID**

**Метод:** `GET`  
**URL:** `/sales/{saleId}`  

**Описание:** Возвращает информацию о продаже по ее ID.

### Пример запроса:
`GET /sales/1`

## **3. Создать новую продажу**

**Метод:** `POST`  
**URL:** `/sales/`  

**Описание:** Создает новую продажу.

### Тело запроса (JSON):
``
{
  "documentName": "Документ продажи",
  "productId": 1,
  "quantity": 10
}
``

## **4. Обновить информацию о продаже**

**Метод:** `PATCH`  
**URL:** `/sales/{saleId}`  

**Описание:** Обновляет данные о продаже.

### Тело запроса (JSON):
``
{
  "documentName": "Обновленный документ",
  "quantity": 5
}
``

## **5. Удалить продажу**

**Метод:** `DELETE`  
**URL:** `/sales/{saleId}`  

**Описание:** Удаляет продажу по указанному ID.

### Пример запроса:
``
DELETE /sales/1
``

## Примечания

1. В запросах на создание и обновление продаж quantity должно быть больше 0.
2. Нельзя продавать больше товара, чем доступно на складе.
3. Все продукты, на которые ссылаются продажи, должны существовать в базе данных.

# Управление поставками

## Базовый URL:
`http://localhost:8080/supplies/`

---

## **1. Получить список всех поставок**

**Метод:** `GET`  
**URL:** `/supplies/`  

**Описание:** Возвращает список всех поставок.

### Пример запроса:
`GET /supplies/`

## **2. Получить поставку по ID**

**Метод:** `GET`  
**URL:** `/supplies/{supplyId}`  

**Описание:** Возвращает информацию о поставке по ее ID.

### Пример запроса:
`GET /supplies/1`

## **3. Создать новую поставку**

**Метод:** `POST`  
**URL:** `/supplies/`  

**Описание:** Создает новую поставку.

### Тело запроса (JSON):
``
{
  "documentName": "Документ поставки",
  "productId": 1,
  "quantity": 50
}
``

## **4. Обновить информацию о поставке**

**Метод:** `PATCH`  
**URL:** `/supplies/{supplyId}`  

**Описание:** Обновляет данные о поставке.

### Тело запроса (JSON):
``
{
"documentName": "Обновленный документ",
  "quantity": 100
}
``

## **5. Удалить поставку**

**Метод:** `DELETE`  
**URL:** `/supplies/{supplyId}`  

**Описание:** Удаляет поставку по указанному ID.

### Тело запроса (JSON):
``
DELETE /supplies/1
``
## Примечания

1. В запросах на создание и обновление поставок quantity должно быть больше 0.
2. Все продукты, на которые ссылаются поставки, должны существовать в базе данных.
3. documentName ограничено 255 символами.




