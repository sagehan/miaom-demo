(ns miaomfood.datomic.data
  (:require
      [datomic.api :as d]))

(def data-tx
  [
   {:db/id (d/tempid :db.part/user -1)
    :website/title "喵姆餐厅 - 乌鲁木齐全城外送"}
   {:db/id (d/tempid :db.part/user -2)
    :groups/menus [#db/id[:db.part/user -3]]}
   {:db/id (d/tempid :db.part/user -3)
    :menu/name "好吃的"
    :menu/categories [#db/id[:db.part/user -5]
                           #db/id[:db.part/user -6]
                           #db/id[:db.part/user -7]]}
   {:db/id (d/tempid :db.part/user -5)
    :category/name "披萨"
    :category/cuisines [#db/id[:db.part/user -8]
                        #db/id[:db.part/user -9]]}
   {:db/id (d/tempid :db.part/user -6)
    :category/name "焗饭"}
   {:db/id (d/tempid :db.part/user -7)
    :category/name "沙拉"}
   {:db/id (d/tempid :db.part/user -8)
    :cuisine/name "激情培根"
    :cuisine/species [{:db/id #db/id [:db.part/user -10]
                       :spec/name :spec.name/八寸
                       :currency/Abbr :currency.Abbr/CNY
                       :spec/price 39
                       :spec/inventory 100}
                      {:db/id #db/id [:db.part/user -11]
                       :spec/name :spec.name/十寸
                       :currency/Abbr :currency.Abbr/CNY
                       :spec/price 59
                       :spec/inventory 100}
                      ]}
   {:db/id (d/tempid :db.part/user -9)
    :cuisine/name "风情夏威夷"
    :cuisine/species [{:db/id #db/id [:db.part/user -12]
                       :spec/name :spec.name/八寸
                       :currency/Abbr :currency.Abbr/CNY
                       :spec/price 39
                       :spec/inventory 100}
                      {:db/id #db/id [:db.part/user -13]
                       :spec/name :spec.name/十寸
                       :currency/Abbr :currency.Abbr/CNY
                       :spec/price 59
                       :spec/inventory 100}
                      ]}
   ])

(def schema-tx [
 ;;;; App settings
 ;;; website
   {:db/id #db/id[:db.part/db]
    :db/ident :website/title
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "网站标题"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :website/notices
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "布告栏"
    :db.install/_attribute :db.part/db}
   ;; notices
   {:db/id #db/id[:db.part/db]
    :db/ident :notice/content
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "布告内容"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :notice/duration
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "展示时长，单位秒"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :notice/priority
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "布告优先级"
    :db.install/_attribute :db.part/db}

 ;;;; Golobal settings
   {:db/id #db/id[db.part/db]
    :db/ident :threshold/oldCustomer
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "熟客门槛"
    :db.install/_attribute :db.part/db}

 ;;;; menu
   {:db/id #db/id[:db.part/db]
    :db/ident :groups/menus
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :menu/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "分组名称"
    :db/unique :db.unique/identity
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :menu/categories
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "所含菜品种类"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :category/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "品类名称"
    :db/unique :db.unique/identity
    :db/fulltext true
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :category/cuisines
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "所含菜品"
    :db.install/_attribute :db.part/db}

 ;;; restaurant
   {:db/id #db/id[:db.part/db]
    :db/ident :restaurant/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "餐厅名称"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :restaurant/phone
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "餐厅电话"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :restaurant/wechat
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "餐厅微信ID"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :restaurant/address
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "餐厅地址"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :restaurant/isClosed
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one
    :db/doc "是否打烊"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :restaurant/rest-day
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "休息日，枚举型，'周一','周二',等等"
    :db.install/_attribute :db.part/db}

 ;;; cuisine
   {:db/id #db/id[:db.part/db]
    :db/ident :cuisine/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/fulltext true
    :db/doc "菜品名称"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :cuisine/shortname
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/fulltext true
    :db/doc "菜品简称"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :cuisine/doc
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/many
    :db/fulltext true
    :db/doc "菜品描述"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :cuisine/depict
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/many
    :db/doc "菜品图片文件名"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :cuisine/species
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/isComponent true
    :db/doc "菜品规格"
    :db.install/_attribute :db.part/db}

   ;; Spec
   {:db/id #db/id[:db.part/db]
    :db/ident :spec/name
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "规格，枚举型，'10寸'，'8寸'，等等"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :currency/Abbr
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "币种，枚举型，'人民币'等等"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :spec/duplex
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "双拼对象"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :spec/price
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "售价"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :spec/inventory
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/noHistory true
    :db/doc "库存"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :spec/quantity
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/noHistory true
    :db/doc "订购量"
    :db.install/_attribute :db.part/db}

   ;; Enums
   [:db/add #db/id[:db.part/user] :db/ident :spec.name/八寸]
   [:db/add #db/id[:db.part/user] :db/ident :spec.name/十寸]
   [:db/add #db/id[:db.part/user] :db/ident :spec.name/买五赠一]

   [:db/add #db/id[:db.part/user] :db/ident :currency.Abbr/CNY]

;;; customer
   {:db/id #db/id[:db.part/db]
    :db/ident :customer/phone
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/fulltext true
    :db/doc "手机号"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/username
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/fulltext true
    :db/doc "用户名"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/gender
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "性别"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/streetAddress
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/fulltext true
    :db/doc "地址"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/email
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/fulltext true
    :db/doc "邮件"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/wechat
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/fulltext true
    :db/doc "微信ID"
    :db.install/_attribute :db.part/db}


   {:db/id #db/id[:db.part/db]
    :db/ident :customer/qq
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/fulltext true
    :db/doc "QQ号"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/password
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "哈希密码"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/total-consumption
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/noHistory true
    :db/doc "消费总额"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/total-purchase
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/noHistory true
    :db/doc "订购次数"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/likes
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "喜爱的菜品"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/total-isNew
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one
    :db/doc "是否新用户"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/total-isRegular
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one
    :db/doc "是否熟客"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/total-isSuspicious
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one
    :db/doc "是否可疑"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/total-isBlocked
    :db/valueType :db.type/boolean
    :db/cardinality :db.cardinality/one
    :db/doc "是否被锁"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :customer/ip
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/many
    :db/doc "源ip"
    :db.install/_attribute :db.part/db}

;;;; order
   {:db/id #db/id[:db.part/db]
    :db/ident :order/tokenSlug
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/value
    :db/fulltext true
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :order/customer
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "订单顾客"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :order/cuisines
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/isComponent true
    :db/doc "已订菜品"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :order/coupons
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/isComponent true
    :db/doc "促销码"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :order/total
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db/doc "订单总额"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :order/comment
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/fulltext true
    :db/doc "订单备注"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :order/payment
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/isComponent true
    :db/doc "支付方式"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :order/schedule-day
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "预订日期，枚举，'今日'，'明日'"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :order/schedule-time
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "预订时间，单位秒数"
    :db.install/_attribute :db.part/db}

   ;; coupon
   {:db/id #db/id[:db.part/db]
    :db/ident :coupon/code
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/fulltext true
    :db/doc "促销码"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :coupon/min-total
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "促销码允许使用的最小订单总额"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :coupon/discounting
    :db/valueType :db.type/float
    :db/cardinality :db.cardinality/one
    :db/doc "折扣幅度"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :coupon/operator
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "折扣方式"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :coupon/quota
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "促销码数量"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :coupon/deadline
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one
    :db/doc "促销码失效日期，单位Unix时间戳"
    :db.install/_attribute :db.part/db}

   ;; payment
   {:db/id #db/id[:db.part/db]
    :db/ident :payment/receiver
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "收款方或到付，枚举型，'到付'，'微信支付', '支付宝'"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :payment/transaction-ID
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/fulltext true
    :db/doc "支付平台结算流水号"
    :db.install/_attribute :db.part/db}

;;;; Enums
   [:db/add #db/id[:db.part/user] :db/ident :rest-day/周一]
   [:db/add #db/id[:db.part/user] :db/ident :rest-day/周二]
   [:db/add #db/id[:db.part/user] :db/ident :rest-day/周三]
   [:db/add #db/id[:db.part/user] :db/ident :rest-day/周四]
   [:db/add #db/id[:db.part/user] :db/ident :rest-day/周五]
   [:db/add #db/id[:db.part/user] :db/ident :rest-day/周六]
   [:db/add #db/id[:db.part/user] :db/ident :rest-day/周日]

   [:db/add #db/id[:db.part/user] :db/ident :gender/男士]
   [:db/add #db/id[:db.part/user] :db/ident :gender/女士]
   [:db/add #db/id[:db.part/user] :db/ident :gender/其她]

   [:db/add #db/id[:db.part/user] :db/ident :order.schedule-day/今日]
   [:db/add #db/id[:db.part/user] :db/ident :order.schedule-day/明日]

   [:db/add #db/id[:db.part/user] :db/ident :cupon.operator/直减]
   [:db/add #db/id[:db.part/user] :db/ident :cupon.operator/打折]

   [:db/add #db/id[:db.part/user] :db/ident :payment.receiver/到付]
   [:db/add #db/id[:db.part/user] :db/ident :payment.receiver/微信支付]
   [:db/add #db/id[:db.part/user] :db/ident :payment.receiver/支付宝]])
