package bj.discogsbrowser.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import bj.discogsbrowser.model.listing.ScrapeListing;

/**
 * Created by Josh Laird on 12/04/2017.
 * <p>
 * Due to Discogs making their marketplace search API endpoint private I have to go to the website and scrape the information.
 * It only searches for 12" records.
 */
public class DiscogsScraper
{
    public ArrayList<ScrapeListing> scrapeListings(String id) throws IOException
    {
        ArrayList<ScrapeListing> scrapeListings = new ArrayList<>();
        Document doc = Jsoup
                .connect("http://www.discogs.com/sell/list?sort=price%2Casc&limit=50&ev=mbformat_desc=12\"&release_id=" + id)
                .get();
        Elements marketplaceListings = doc.getElementsByClass("shortcut_navigable ");
        for (Element element : marketplaceListings)
        {
            // Contains price details
            String price = element.getElementsByClass("hide_desktop").get(0).getElementsByClass("price").get(0).text();
            String convertedPrice = "";
            try
            {
                convertedPrice = "(" + element.getElementsByClass("hide_desktop").get(0).getElementsByClass("converted_price").get(0).text().split(" ")[1] + ")";
            }
            catch (IndexOutOfBoundsException e)
            {
                // Listed in local price. No need to convert
            }
            // Contains media and sleeve condition
            Element itemDescription = element.getElementsByClass("item_description").get(0);
            String mediaCondition = itemDescription.getElementsByClass("icon icon-info-circle muted media-condition-tooltip").get(0).attr("data-condition");
            String sleeveCondition = "n/a";
            try
            {
                sleeveCondition = itemDescription.getElementsByClass("item_sleeve_condition").get(0).text();
            }
            catch (IndexOutOfBoundsException e)
            {
                // No sleeve
            }

            Element sellerInfo = element.getElementsByClass("seller_info").get(0);
            String sellerUrl = sellerInfo.getElementsByTag("a").attr("href");
            String sellerName = sellerInfo.getElementsByTag("a").text().split(" ")[0];
            String sellerRating = "No rating";
            try
            {
                sellerRating = sellerInfo.getElementsByTag("strong").get(1).text();
            }
            catch (IndexOutOfBoundsException e)
            {
                // New account, no rating
            }
            String shipsFrom = element.getElementsByTag("li").get(2).text().replace("Ships From:", "From: ");

            String marketPlaceId = element.getElementsByClass("button button_green cart_button ").attr("data-item-id");
            if (marketPlaceId.equals(""))
                // Discogs' page HTML is sometimes weird af
                marketPlaceId = element.getElementsByClass("button button_green cart_button confirm-add-to-cart").attr("data-item-id");

            scrapeListings.add(new ScrapeListing(price, convertedPrice, mediaCondition, sleeveCondition, sellerUrl, sellerName, sellerRating, shipsFrom, marketPlaceId));
        }
        return scrapeListings;
    }
}
