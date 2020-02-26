package com.brain.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    /**
     * Completely run parser.
     * Parse ex-fs.com site page and save it to 'parsed' folder
     */
    public void run() {
        System.out.println("Executing run method");
        new File("parsed").mkdir(); // создаем папку в корне проекта
        List<String> urls = parse(); // вызываем метод парсинга URL картинок
        save(urls); // Сохранение картинок из списка ссылок.
    }

    /**
     * Parse film posters of ex-fs.com site page.
     * @return img URLs list
     */
    private List<String> parse() {
        System.out.println("Parsing page");
        String url = "https://www.kinonews.ru"; // URL страницы
        String urlPage = url + "/top100"; // URL страницы
        List<String> result = new ArrayList<>(); // пустой обьект списка
        try { // блок try...catch для обработки ошибок
            Document document = Jsoup.connect(urlPage).get(); // обьект HTML страницы
            Elements links = document.select(".rating_leftposter img[src]"); // css селектор
            for (Element link : links) { // цикл по выбранным елементам
                // строим URL картинки:
                // 1. берем host сайта
                // 2. добавляем к нему значение источника картинки (в теге img у аттрибута src)
                String imgUrl = url + link.attr("src");
                result.add(imgUrl); // добавляем URL картинки к результатам
                System.out.println(imgUrl);
            }
        } catch (IOException e) { // обработка возможной ошибки
            e.printStackTrace();
        }
        return result; // возвращение результата метода
    }

    /**
     * Save all images from URL to disk.
     * @param urls URLs list.
     */
    private void save(List<String> urls) {
        System.out.println("Save");
        for (int i = 0; i < urls.size(); i++) { // цикл по списку из ссылок на картинки
            try {
                String imgUrl = urls.get(i); // получаем i-ю строку с ссылкой
                URL url = new URL(urls.get(i)); // создаем объект URL из строки
                BufferedImage image = ImageIO.read(url); // читаем картинку
                String imgType = imgUrl.substring(imgUrl.lastIndexOf('.') + 1); // выделяем тип
                //Сохраняем картинку на диск с новым именем
                ImageIO.write(image, imgType, new File("parsed/img_" + i + '.' + imgType));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
