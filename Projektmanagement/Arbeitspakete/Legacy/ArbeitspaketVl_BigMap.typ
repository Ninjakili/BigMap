#set page(
  "a4",
  margin: 1.0cm,
)
#let today = datetime.today()

#align(center)[#underline[= Arbeitspaketbeschreibung]]
#linebreak()

#align(center)[
  #grid(
      columns:(7.5fr, 2.5fr),
      rows:(auto, auto),
      align: center,
      [#table(
        columns: (1fr),
        //inset: 10pt,
        align: left,
        [*Titel:* 11111],
        [*Aktivitäten:*
          - placeholder
          - placeholder
          - placeholder
          - placeholder],
      )],
      [#table(
        columns: (1fr),
        //inset: 10pt,
        align: left,
        [*PSP-Code:* 11111],
        [*Datum:* 11111],
        [*Verantwortlich:* 111111],
        [*Beteiligt:* 111111],
        [*Start:* 111111],
        [*Ende:* 111111],
      )],
  )
  #table(
    columns: (1fr),
    align: left,
    [test]
  )
]
