# BeatBot [![Build Status](https://travis-ci.org/davipatury/BeatBot.svg?branch=master)](https://travis-ci.org/davipatury/BeatBot)
BeatBot é um bot de música para [Discord](discordapp.com). Ele pode tocar músicas locais ou de sites como Youtube e Soundcloud.

### Requerimentos
- Java 8

### Instalação
- [Baixe](https://github.com/davipatury/BeatBot/releases) o jar e seus complementos
- Transfira para o seu server ou use ele no seu computador
- Configure o bot através do config.yml
- Use este comando para rodá-lo `java -jar BeatBot.jar`

### Uso
- `!help` - Mostra todos os comandos, você pode usar !help [nome do comando] para mais informações.
- `!play <url>` - Adiciona um vídeo do Youtube / Soundcloud à playlist
- `!skip` - Pula para a próxima música na playlist
- `!queue` - Mostra a playlist atual

### Used third party libraries
| Name | Version |
|------|---------|
| [JDA](https://github.com/DV8FromTheWorld/JDA)  | 1.2.2_139 |
| [JDAPlayer](https://github.com/ronmamo/reflections) | 0.9.9 |
| [VLCJ](https://github.com/caprica/vlcj) | 3.8.0 |

### Licença
Este projeto é licenciado sobre o GNU General Public License v3.0. Veja o arquivo LICENSE para mais detalhes.
