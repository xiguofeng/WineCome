/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xgf.winecome.qrcode.google.zxing.client.result.supplement;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xgf.winecome.R;
import com.xgf.winecome.qrcode.google.zxing.client.HttpHelper;
import com.xgf.winecome.qrcode.google.zxing.client.LocaleManager;
import com.xgf.winecome.qrcode.google.zxing.client.history.HistoryManager;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

final class ProductResultInfoRetriever extends SupplementalInfoRetriever {

  private static final Pattern[] PRODUCT_NAME_PRICE_PATTERNS = {
    Pattern.compile(",event\\)\">([^<]+)</a></h3>.+<span class=psrp>([^<]+)</span>"),
    Pattern.compile("owb63p\">([^<]+).+zdi3pb\">([^<]+)"),
  };

  private final String productID;
  private final String source;
  private final Context context;

  ProductResultInfoRetriever(TextView textView, String productID, HistoryManager historyManager, Context context) {
    super(textView, historyManager);
    this.productID = productID;
    this.source = context.getString(R.string.msg_google_product);
    this.context = context;
  }

  @Override
  void retrieveSupplementalInfo() throws IOException {

    String encodedProductID = URLEncoder.encode(productID, "UTF-8");
    String uri = "http://www.google." + LocaleManager.getProductSearchCountryTLD(context)
            + "/m/products?ie=utf8&oe=utf8&scoring=p&source=zxing&q=" + encodedProductID;
    CharSequence content = HttpHelper.downloadViaHttp(uri, HttpHelper.ContentType.HTML);

    for (Pattern p : PRODUCT_NAME_PRICE_PATTERNS) {
      Matcher matcher = p.matcher(content);
      if (matcher.find()) {
        append(productID,
               source,
               new String[] { unescapeHTML(matcher.group(1)), unescapeHTML(matcher.group(2)) },
               uri);
        break;
      }
    }
  }

  private static String unescapeHTML(String raw) {
    return Html.fromHtml(raw).toString();
  }

}
