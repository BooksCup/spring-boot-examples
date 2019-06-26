# spring-boot-elasticsearch
springboot集成elasticsearch的例子  
场景:对商品的CRUD  

* `POST /goods`&nbsp;&nbsp;&nbsp;&emsp;&emsp;&emsp;&emsp;&emsp;&nbsp;新增商品  
* `GET /goods/v1` &nbsp;&nbsp;&nbsp;&emsp;&emsp;&emsp;&emsp;查询商品(简单query)  
* `GET /goods/v2` &nbsp;&nbsp;&nbsp;&emsp;&emsp;&emsp;&emsp;查询商品(query + fliter)  
* `GET /goods/v3` &nbsp;&nbsp;&nbsp;&emsp;&emsp;&emsp;&emsp;查询商品(高亮highlight)  
* `DELETE /goods/{goodsId}` &nbsp;删除商品  
* `GET /goodsSuggests`&emsp;&emsp;&nbsp;&nbsp;&nbsp;商品搜索词补齐
