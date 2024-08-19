package com.example.aplicacion2
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        webView = findViewById(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true

        // Obtener datos de las vistas anteriores
        val anio = intent.getStringExtra("anio") ?: "2022"
        val depto = intent.getStringExtra("depto") ?: "900"
        val muni = intent.getStringExtra("muni") ?: "901"
        val proy = intent.getStringExtra("proy") ?: "cpe-019"

        // Construir la URL
        val url = "https://apps.covial.gob.gt/sitiopublicomovilrv/Consultas?anio=$anio&depto=$depto&muni=$muni&proy=$proy"

        // Cargar la URL en el WebView
        webView.loadUrl(url)
    }
}