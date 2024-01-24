package com.example.myapplication

import javax.persistence.*

@Entity
data class News(
    @Id
    @javax.persistence.GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    var id: Long? = null,

    @Column(name = "news_title")
    var title: String? = null,

    @Column(name = "news_category")
    var category: Int = 0,

    @Column(name = "news_description")
    var description: String? = null,

    @Column(name = "news_url")
    var url: String? = null,

    @Column(name = "news_image")
    var image: String? = null
)
