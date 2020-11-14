# SimpleSlider
This is a Minecraft(JE) Spigot plugin which makes it easier for players to teleport anywhere without any permissions within the same WorldGuard region.

Inspired by [SimpleElevators](https://www.spigotmc.org/resources/simple-elevators-1-8-1-16.44462/) (made by benzoft56), and [this idea ticket](https://red.minecraftserver.jp/issues/8575) ([seichi.click](https://www.seichi.network)). Thanks!

## Usage
* /hoe コマンドで専用の木のクワを取得
* スライダーを設置
    * global保護なら`config.yml`の`search-max-distance`で指定された値の範囲
    * 保護が1つだけあるならその保護の境界までを検索する
    * 保護が2つ以上ある場合は検索しない
    * スライダーの種類が違うスライダー間では反応しない
* 専用クワを持って、行きたい方向へ向かって右クリック

### スライダーの種類
|感圧板|下に設置するブロック|特殊効果|
|----|----|----|
|重量感圧板|鉄ブロック|なし|
|軽量感圧板|金ブロック|なし|
|重量感圧板|エメラルドブロック|移動速度上昇（60s）|
|重量感圧板|ダイヤモンドブロック|浮遊（3s）したあと下降（3s）|
|重量感圧板|ネザー水晶ブロック|跳躍力上昇（60s）|

## Requirement
* Spigot
    * Developed and debugged in 1.12.2
* [item-nbt-api-plugin-1.8.2-SNAPSHOT](https://www.spigotmc.org/resources/item-entity-tile-nbt-api.7939/download?version=241690)
* [worldguard-bukkit-6.2.2](https://dev.bukkit.org/projects/worldguard/files/2610618/download)

## License
[GNU General Public License v3.0](./LICENSE)
