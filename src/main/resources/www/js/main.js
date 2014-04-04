(function (global) {
  $(function ($) {
    // parse query string
    // http://stackoverflow.com/questions/901115/how-can-i-get-query-string-values-in-javascript
    function getParameterByName(name) {
      name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
      var regex = new RegExp('[\\?&]' + name + '=([^&#]*)'),
        results = regex.exec(location.search);
      return results == null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    }
    
    // get data
    var testData = {
      Results: [{
        "Brand": {},
        "UPCs": [],
        "ISBNs": [],
        "Description": "Stream HD video, play games and multitask simultaneously with this wireless router that delivers fast connectivity and data transfer. Advanced security features protect your network from intruders while you surf the Web. This product has been refurbished by the manufacturer. Learn more.",
        "Attributes": {},
        "ManufacturerPartNumbers": [],
        "QuestionIds": [],
        "ProductPageUrl": null,
        "BrandExternalId": "9969l3qgcm5y7c5y2bpmydfa",
        "FamilyIds": [],
        "AttributesOrder": [
          "INVALID_EAN"
        ],
        "ImageUrl": "http://images.bestbuy.com/BestBuy_US/images/products/9999/9999941_rc.jpg",
        "EANs": [],
        "Name": "NETGEAR Factory-Refurbished RangeMax Wireless-N Gigabit Router w/ 4-Port Ethernet Switch",
        "CategoryId": "pcmcat224100050011",
        "ReviewIds": [],
        "Id": "9999941",
        "StoryIds": [],
        "ModelNumbers": [
          "WNR3500-100NAR"
        ]
      }]
    };

    var urlTemplate = Handlebars.compile([
      '/data/products.json?',
      'rating={{rating}}&',
      'user={{user}}&',
      'client={{client}}'
    ].join(''));

    $.getJSON(urlTemplate({
        rating: getParameterByName('rating'),
        user: getParameterByName('user'),
        client: getParameterByName('client')
      })).
      then(function (data) {
        // render it
        var source = $("#template").html();
        var template = Handlebars.compile(source);

        $('.main').html(template(data));
      });
  });
}(this));
