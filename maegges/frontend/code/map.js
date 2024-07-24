async function fetchXML(url) {
  const response = await fetch(url);
  return response.text();
}

async function fetchXSL(url) {
  const response = await fetch(url);
  return response.text();
}

async function transformXMLWithXSL(xml, xsl) {
  const parser = new DOMParser();
  const xmlDoc = parser.parseFromString(xml, "application/xml");
  const xslDoc = parser.parseFromString(xsl, "application/xml");

  const xsltProcessor = new XSLTProcessor();
  xsltProcessor.importStylesheet(xslDoc);
  const resultDocument = xsltProcessor.transformToDocument(xmlDoc);

  const serializer = new XMLSerializer();
  return serializer.serializeToString(resultDocument);
}

async function drawNodesAndPolys() {
  let [xmlData, xslData] = await Promise.all([
    fetch("/getPolys", {
      headers: { "Content-Type": "application/json" },
      method: "POST",
      mode: "cors",
      body: JSON.stringify({ id: user }),
    })
      .then((response) => response.text())
      .then((data) => new window.DOMParser().parseFromString(data, "text/xml")),
    fetchXSL("/transformpolys.xsl"),
  ]);

  // Transform XML to GPX
  xmlData = new XMLSerializer().serializeToString(xmlData.documentElement);
  const transformedGPX = await transformXMLWithXSL(xmlData, xslData);

  // Create a vector layer using the transformed GPX data
  const polySource = new ol.source.Vector({
    url: "data:text/xml;charset=UTF-8," + encodeURIComponent(transformedGPX),
    format: new ol.format.GPX(),
  });

  [xmlData, xslData] = await Promise.all([
    fetch("/getNodes", {
      headers: { "Content-Type": "application/json" },
      method: "POST",
      mode: "cors",
      body: JSON.stringify({ id: user }),
    })
      .then((response) => response.text())
      .then((data) => new window.DOMParser().parseFromString(data, "text/xml")),
    fetchXSL("/transformnodes.xsl"),
  ]);

  // Transform XML to GPX
  xmlData = new XMLSerializer().serializeToString(xmlData.documentElement);
  const transformedNodeGPX = await transformXMLWithXSL(xmlData, xslData);

  // Create a vector layer using the transformed GPX data
  const nodeSource = new ol.source.Vector({
    url:
      "data:text/xml;charset=UTF-8," + encodeURIComponent(transformedNodeGPX),
    format: new ol.format.GPX({
      readExtensions: function (feature, extensionsNode) {
        const extensions = {};
        let street = "",
          housenumber = "",
          postcode = "",
          city = "",
          address,
          hours,
          delivery,
          phone,
          contact_phone,
          service_mail,
          rating,
          review,
          avg;
        for (
          let child = extensionsNode.firstElementChild;
          child;
          child = child.nextElementSibling
        ) {
          extensions[child.localName] = child.textContent;
        }
        for (var k in extensions) {
          if (typeof extensions[k] !== "function") {
            let v = extensions[k];
            if (k === "addr_street") street = v;
            if (k === "addr_housenumber") housenumber = v;
            if (k === "addr_postcode") postcode = v;
            if (k === "addr_city") city = v;
            if (k === "opening_hours") hours = v;
            if (k === "website") website = v;
            if (k === "contact_website") contact_website = v;
            if (k === "delivery") delivery = v;
            if (k === "phone") phone = v;
            if (k === "contact_phone") contact_phone = v;
            if (k === "service_mail") service_mail = v;
            if (k === "rating") rating = v;
            if (k === "review") review = v;
            if (k === "avg") avg = v;
            feature.set(k, v);
          }
        }
        address = `${street} ${housenumber}, ${postcode} ${city}`;

        feature.set("address", address);

        let popupContentHTML = `<b>McDonald's</b><br>${address}`;
        if (hours) popupContentHTML += `<br>Opening hours: ${hours}`;
        if (delivery) popupContentHTML += `<br>Delivery: ${delivery}`;
        if (phone) popupContentHTML += `<br>Telephone: ${phone}`;
        if (contact_phone) popupContentHTML += `<br>Telephone: ${contact_phone}`;
        if (service_mail)
          popupContentHTML += `<br>Service Mail: ${service_mail}`;
        if(avg)
          popupContentHTML += `<br>Rating ${avg} (1-5): `
        else
          popupContentHTML += `<br>Rating (1-5): `
        if (rating)
          popupContentHTML += `<input id="rating" placeholder="${rating}" class="rating"></input><br>`;
        else
          popupContentHTML += `<input id="rating" class="rating"></input><br>`;
        if (review)
          popupContentHTML += `<br>Review: <textarea id="review" placeholder="${review}" class="review"></textarea><hr>`;
        else
          popupContentHTML += `<br>Review: <textarea id="review" class="review"></textarea><hr>`;
        popupContentHTML += `<button class="popupButtonLeft" onClick="toggleVisited(${feature
          .get("name")
          .substr(5)})">Toggle Visit</button>`;
        popupContentHTML += `<button class="popupButtonRight" onClick="giveReview(${feature
          .get("name")
          .substr(5)})">Submit Review</button>`;

        feature.set("popupContent", popupContentHTML);

        return extensions;
      },
    }),
  });

  polySource.once("change", function (evt) {
    const state = polySource.getState();
    if (state === "ready") {
      const features = evt.target.getFeatures();
      features.forEach((feature) => {
        let geometry = feature.getGeometry();
        if (geometry) {
          let geometryType = geometry.getType();

          if (
            geometryType === "LineString" ||
            geometryType === "MultiLineString"
          ) {
            let coordinates = geometry.getCoordinates()[0];
            if (
              coordinates.length > 2 &&
              coordinates[0][0] == coordinates[coordinates.length - 1][0] &&
              coordinates[0][1] == coordinates[coordinates.length - 1][1]
            ) {
              feature.setGeometry(new ol.geom.Polygon([coordinates]));
            }
          }
        }
      });
    }
    polySource.refresh(); // Refresh the source to apply changes
  });

  polyLayer.setSource(polySource);

  nodeLayer.setSource(nodeSource);
  polyLayer.setSource(polySource);
}

const styleFunction = function (feature) {
  let geometryType = feature.getGeometry().getType();
  if (geometryType === "Polygon") {
    return new ol.style.Style({
      stroke: new ol.style.Stroke({
        color: "green",
        width: 3,
      }),
      fill: new ol.style.Fill({
        color: "rgba(0, 255, 0, 0.2)",
      }),
    });
  }
  return new ol.style.Style({
    stroke: new ol.style.Stroke({
      color: "#FF0000",
      width: 3,
    }),
  });
};

const polyLayer = new ol.layer.Vector({
  style: styleFunction,
});

const nodeLayer = new ol.layer.Vector({
  style: new ol.style.Style({
    image: new ol.style.Circle({
      radius: 7,
      fill: new ol.style.Fill({
        color: "red",
      }),
    }),
  }),
});

// Define the world extent in EPSG:3857
const worldExtent = [-20037508.34, -20037508.34, 20037508.34, 20037508.34];

// Create a tile grid with limited extent
const tileGrid = new ol.tilegrid.TileGrid({
  extent: worldExtent,
  resolutions: ol.tilegrid.createXYZ().getResolutions(),
  tileSize: 256,
});

// Define the map view with constrained extent
const view = new ol.View({
  center: ol.proj.fromLonLat(
    ol.proj.transform(
      [933697.1020563837, 6278907.216863753],
      "EPSG:3857",
      "EPSG:4326"
    )
  ),
  zoom: 12,
  minZoom: 7.5,
  maxZoom: 18, // Adjust this value as necessary
  constrainResolution: true,
  extent: ol.proj.transformExtent(
    [5.5, 47.4, 15, 55], // Global extent in EPSG:4326
    "EPSG:4326",
    "EPSG:3857"
  ),
});

const map = new ol.Map({
  layers: [
    new ol.layer.Tile({
      source: new ol.source.OSM({
        wrapX: false,
        tileGrid: tileGrid,
      }),
    }),
    //layer2,
    //layer,
    //polyLayer,
  ],
  target: "map",
  view: view,
});

map.addLayer(polyLayer);
map.addLayer(nodeLayer);

// Create a single overlay for the popup
const popupElement = document.createElement("div");
popupElement.className = "popup";
const popupContent = document.createElement("div");
popupContent.className = "popup-content";
const popupCloser = document.createElement("div");
popupCloser.className = "popup-closer";
popupCloser.innerHTML = "✖";
popupElement.appendChild(popupCloser);
popupElement.appendChild(popupContent);

const overlay = new ol.Overlay({
  element: popupElement,
  autoPan: true,
  autoPanAnimation: {
    duration: 250,
  },
});
map.addOverlay(overlay);

popupCloser.onclick = function () {
  overlay.setPosition(undefined);
  popupCloser.blur();
  return false;
};

map.on("singleclick", function (evt) {
  const feature = map.forEachFeatureAtPixel(evt.pixel, function (feature) {
    return feature;
  });
  if (feature) {
    console.log(feature);
    if (feature.get("name").startsWith("node_")) {
      const coordinates = feature.getGeometry().getCoordinates();
      const content = feature.get("popupContent");
      view.animate({
        center: coordinates,
        duration: 1000,
        zoom: 14,
      });
      popupContent.innerHTML = content;
      overlay.setPosition(coordinates);
    } else {
      const all = nodeLayer.getSource().getFeatures();
      let thisID = feature.get("name");
      console.log(thisID);
      all.forEach((one) => {
        const otherID = one.get("name").substr(5);
        if (thisID == otherID) {
          console.log("found");
          const coordinates = one.getGeometry().getCoordinates();
          const content = one.get("popupContent");
          view.animate({
            center: coordinates,
            duration: 1000,
            zoom: 12,
          });
          popupContent.innerHTML = content;
          overlay.setPosition(coordinates);
        }
      });
    }
  }
});

// Implement search functionality
document.getElementById("search-button").addEventListener("click", function () {
  const searchText = document
    .getElementById("search-input")
    .value.toLowerCase();
  if (searchText) {
    // Search for features matching the search text
    const features = nodeLayer.getSource().getFeatures();
    let found = false;

    features.forEach((feature) => {
      const name = feature.get("name");
      const address = feature.get("address");
      const city = address ? address.split(", ").pop() : "";

      if (
        name &&
        (name.toLowerCase().includes(searchText) ||
          city.toLowerCase().includes(searchText))
      ) {
        // Center the map on the found feature
        const coordinates = feature.getGeometry().getCoordinates();
        flyTo(coordinates, 12);
        found = true;
        // Show popup
        popupContent.innerHTML = feature.get("popupContent");
        overlay.setPosition(coordinates);
        console.log(view.getCenter());
      }
    });

    if (!found) {
      alert("No McDonald's or town found matching the search criteria.");
    }
  }
});

// Optionally, trigger search on Enter key press
document
  .getElementById("search-input")
  .addEventListener("keyup", function (event) {
    if (event.key === "Enter") {
      document.getElementById("search-button").click();
    }
  });

function flyTo(location, destZoom) {
  const duration = 2000;
  const zoom = view.getZoom();
  let parts = 2;
  let called = false;
  view.animate({
    center: location,
    duration: duration,
  });
  view.animate(
    {
      zoom: zoom - 1,
      duration: duration / 2,
    },
    {
      zoom: destZoom,
      duration: duration / 2,
    }
  );
}
