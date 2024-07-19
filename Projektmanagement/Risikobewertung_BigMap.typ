#set page(
  "a4",
  margin: (x: 0pt, y: 4pt),
)

#align(center)[#underline[= Risiken]]
#linebreak()

#set table(
  fill: (x, y) =>
    if y == 0 { red.lighten(60%)},
)

#table(
  //columns: (0.05fr, 0.2fr, 0.1fr, 0.12fr, 0.1fr, 0.1fr, 0.1fr),
  columns: (auto, auto, auto, auto, auto, auto, auto),
  align: left,
  table.header(
    [*Index*], [*Riskio*], [*Ursache*], [*Eintrittsklasse*], [*Schadensklasse*], [*Gegenmaßnahme*], [*Stakeholder*]
  ),
  [0], [Kurzzeitiger Ausfall Projektmitarbeiter], [Krankheit], [4(\<10%)], [2(>10€)], [Umverteilung Aufgabenlast], [Projektteam],
  [1], [Dauerhafter Ausfall Projektmitarbeiter], [Kündigung], [3(\<5%)], [4(>100€)], [Umverteilung Aufgabenlast, Projektumfang kürzen], [Auftraggeber, Projektteam],
  [2], [Fehleinschätzung des Entwicklungsaufwands], [Unerfahrenheit des Projektteams], [4(\<10%)], [3(>50€)], [Enge Kommunikation Entwickler und Projektleitung, Projektumfang kürzen], [Auftraggeber, Projektteam],
  [3], [Terminkonflikte], [Aufwand für Klausuren, projektexterne Präsentationen], [5(>20%)], [2(>10€)], [Alternativtermine finden, Hybride Meetings, Umverteilung Aufgabenlast, Projektumfang kürzen], [Auftraggeber, Projektteam],
  [4], [Motivationsprobleme], [Stress, Überlastung, fehlende Anerkennung], [3(\<5%)], [3(>50€)], [Kommunikation mit Team und Gespräch mit Projektleitung, Umverteilung Aufgabenlast], [Projektteam],
  [5], [Budgetüberschreitung], [Fehleinschätzung der Hostingkosten und/oder -dauer], [2(\<1%)], [4(>100€)], [Wechsel der Hostingproviders, Verzicht auf Hosting und Erstellung von Anleitung für Localhost], [Projektteam],
)

#underline([== Erklärung])
Für die Risiken die mit das Entwicklungsprojekt BigMap betreffen lassen sich schwer verlagern, können aber vermieden werden.
Um Terminkonflikte zu vermeiden ist beispielsweise rechtzeitige Ankündigung ausschlaggebend. Zudem kann durch den Einsatz von Meeting software wie MS Teams
Flexibilität bzgl. des Standorts geschaffen werden um bei kurzfristigen Absagen Teammitglieder trotzdem einbinden zu können.

Eine Verlagerungsmöglichkeit besteht ggf. bei einer Budgetüberschreitung die durch das Hosting verursacht wird. Vorab kann durch das Team entprechende Dokumentation erstellt werden,
mit der dem Auftraggeber die Verwendung einer lokalen Kopie der Webanwendung ermöglicht wird.

#underline([= Risikomatrix])
#set table(
  stroke: none,
  gutter: 0.2em,
  fill: (x, y) =>
    // unterste reihe = 4
    if y == 4 {
      if x >=2 and x<=5 { green }
      if x ==6 and y == 4 { yellow }
    }
    else if y == 3 {
      if x >= 4 { yellow }
      if x == 2 or x == 3 { green }
    }
    else if y == 2 {
      if x == 2 { green }
      if x == 3 or x == 4 or x == 5 { yellow }
      if x == 6 { red }
    }
    else if y == 1 {
      if x == 2 { green }
      if x == 3 or x == 4 { yellow }
      if x == 5 or x == 6 { red }
    }
    else if y == 0 {
      if x == 2 or x == 3 { yellow }
      if x == 4 or x == 5 or x == 6 { red }
    }
    else if x >=2 and x<=5 and y == 4 { green } else if x ==6 and y == 4 { yellow }
    else if x == 0 and y == 1 { green },
  inset: (right: 1.5em),
)

#align(center)[
  #table(
    columns: 7,
    align: center,
    [5],[>500€],[],[],[],[],[],
    [4],[>100€],[],[5],[1],[],[],
    [3],[>50€],[],[],[4],[],[],
    [2],[>10€],[],[],[],[0, 3],[],
    [1],[\<10€],[],[],[],[],[],
    [],[],[\<0.1%],[\<1,0%],[\<5%],[\<10%],[>20%],
    [],[],[1],[2],[3],[4],[5],
  )
]
