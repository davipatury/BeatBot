# BeatBot [![Build Status](https://travis-ci.org/davipatury/BeatBot.svg?branch=master)](https://travis-ci.org/davipatury/BeatBot)
BeatBot é um bot de música para [Discord](http://www.discordapp.com). Ele pode tocar músicas locais ou de sites como Youtube e Soundcloud.

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

### Third party libraries usadas
| Nome | Versão |
|------|---------|
| [JDA](https://github.com/DV8FromTheWorld/JDA)  | 2.2.1_353 |
| [JDAPlayer](https://github.com/DV8FromTheWorld/JDA-Player) | SNAPSHOT |

### Licença
Este projeto é licenciado sobre o GNU General Public License v3.0. Veja o arquivo LICENSE para mais detalhes.
