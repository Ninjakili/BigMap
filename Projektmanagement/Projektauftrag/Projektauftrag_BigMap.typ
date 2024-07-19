#set page(
  "a4",
  margin: 1.0cm,
)
#let today = datetime.today()

#align(center)[#underline[= Projektauftrag]]
#linebreak()

#align(center)[
  #grid(
      columns:(7.5fr, 2.5fr),
      rows:(auto),
      align: center,
      [#table(
        columns: (1fr),
        //inset: 10pt,
        align: left,
        [*Projektleitung:* Moritz Hasenschwandtner],
        [*Stellvertretendee Projektleitung:* Simon Wehle],
        [*Projektstart:* 04.06.24 \
          *Projektende*: 21.07.24],
      )],
      [#image("ProjektlogoOhneBG_Bigmap.png", width: 50%)]
      )
]

#align(center)[
  #table(
  columns: (1fr, 1fr),
    align: left,
    [=== Auftraggeber: \
      Prof. Dr. Jürgen Röthig (DHBW Karlsruhe)],
    [=== Projektteam: \
      Fabian Huter \
      Kilian Birkhoven \
      Moritz Hasenschwandtner \
      Nils Ettner \
      Simon Wehle \ ],
  )
]

== Projektbeschreibung
#align(center)[
  #table(
    columns: (1fr),
    //inset: 10pt,
    align: left,
    [=== Ist-Analyse und Problembeschreibung: \
      Im Rahmen des Informatikstudiums an der DHBW Kalrsruhe ist ein Projekt im Fach Webengineering durchzuführen. Für das
      Projekt sind vier unterschiedliche Themenbereiche vom Projektauftraggeber vorgegeben: Die Arbeit mit Stammbäumen bzw. Ahnentafeln,
      die Erstellung eines Online-Kalenders, das Erstellen einer Webanwendung für das Darstellen und Bearbeiten einer Begriffswolke bzw. Mindmap,
      oder das Erstellen einer interaktiven Kartenanwendung.

      Im Rahmen dieses Projekts wird eine eine Kartenapplikation im Webbrowser erstellt.

      Um verschiedene Filialen eines gleichen Gastronomie-Franchises übersichtlich bewerten zu können exisitern bisher keine Anwendungen. Damit die
      noch zu besuchenden und bereits bewerteten Filialen visualisiert werden können, und damit deren Beurteilung klar einsehbar ist eignet sich eine
      interaktive Kartenanwendung für die Darstellung im Webbrowser.
    ],
    [=== Ziel der Umsetzung: \
      Die Applikation soll zunächst für alle deutschen Filialen der Fastfood-Kette _McDonalds_ ermöglichen deren Standorte als Stecknadeln auf einer Karte
      zu sehen sowie die zu ihnen gespeicherten Daten durch anklicken zu bearbeiten. Es muss mindestens möglich sein die Filiale als "besucht" markieren
      zu können und eine detaillierte Bewertung inklusive der Vergabe von Sternen (1-5) abgeben zu können. Wurde eine Filiale bewertet, zeigt sich
      dies am Veränderten Aussehen der Stecknadel. Die Karte selbst wird eingefärbt, wenn Filialen in einem bestimmten Bereich bewertet wurden. Der
      Prozentsatz der besuchten Filialen wird berechnet und als Overlay in der Applikation angezeigt.

      Die Applikation muss auf einem Server betrieben werden, damit bis zur Bewertung durch den Auftraggeber auf sie zugegriffen werden kann.

      Sofern im Rahmen der Projektddauer möglich soll die Applikation erweiterbar bzgl. dem Bewerten anderer Franchises sein. Außerdem sollte
      auch Zugriff auf Filialen außerhalb von Deutschland möglich sein.
    ],
    [=== Projektauftrag der Umsetzung: \
      Implementierung der Applikation sowie Betrieb bis zur Abgabe der Benotung durch den Auftraggeber.
    ],
  )
]

#align(center)[
  #table(
    columns: (1fr),
    //inset: 10pt,
    align: left,
    [=== Ausgeschlossene Leistungen \
      Nach Projektende wird die Website nicht mehr gepfleg und Aktualisiert, das heißt es werden keine Updates zur Sicherheit,
      den verwendeten Technologien, Frameworks o.Ä. durchgeführt.
      ],
    [=== Randbedingungen/Technische Voraussetzungen \
      Durch den Auftraggeber wurden folgende technische Anforderungen gestellt:
        + Speichern von Daten in XML-Basierten Format
        + Wandlung von Abstrakten Daten mit der Programmiersprache _XSL Transformation (XSLT)_
        + Darstellung im Browser mit XML-Basierter Sprache
        + Verwendung eines Webservers

      ],
  )
]

#align(center)[
  #table(
  columns: (1fr),
    align: left,
    [#grid(
        columns:(0.16fr, 0.16fr, 0.16fr,0.16fr,0.16fr,0.16fr),
        rows:(20pt, 5pt, auto),
        align: center,
        [],
        [],
        [],
        [],
        [],
        [],
        [#line(length: 3cm)],
        [#line(length: 3cm)],
        [#line(length: 3cm)],
        [#line(length: 3cm)],
        [#line(length: 3cm)],
        [#line(length: 3cm)],
        [_Auftraggeber_],
        [_Projektleitung_],
        [_Stellvertretung_],
        [F. Huter],
        [K. Birkhoven],
        [N. Ettner],
      )
    ],
    [Druckdatum: #today.display("[day].[month].[year]")],
  )
]
