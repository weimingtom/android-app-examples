#include "GameScene.h"

GameScene::GameScene(void)
{
	sGlobal->gameScene = this;
}

GameScene::~GameScene(void)
{
}

bool GameScene::init()
{
	//新建一个GameLayer实例
	GameLayer *gamelayer = GameLayer::node();
	//将GameLayer实例添加到场景中
	this->addChild(gamelayer, kGameLayer, kGameLayer);
	//新建一个ControlLayer实例
	ControlLayer *controlLayer = ControlLayer::node();
	//将ControlLayer实例添加到场景中
	this->addChild(controlLayer, kControlLayer, kControlLayer);
	return true;
}

CCScene* GameScene::playNewGame()
{
	GameScene * scene = NULL;
	do 
    {
		//新游戏，当前地图层数为0
		sGlobal->currentLevel = 0;
		//勇士出生位置
		sGlobal->heroSpawnTileCoord = ccp(1, 11);
		scene = GameScene::node();
		CC_BREAK_IF(! scene);
	} while (0);
	return scene;
}
//切换游戏地图之前
void GameScene::switchMap()
{
	//创建一个遮罩层，用于地图切换时的显示淡入淡出效果
	CCLayerColor* fadeLayer = CCLayerColor::layerWithColor(ccc4(0, 0, 0, 0));
	fadeLayer->setAnchorPoint(CCPointZero);
	fadeLayer->setPosition(CCPointZero);
	addChild(fadeLayer, kFadeLayer, kFadeLayer);
	//执行淡入动画，结束后调用resetGameLayer方法
	CCAction* action = CCSequence::actions(
		CCFadeIn::actionWithDuration(0.5f),
		CCCallFunc::actionWithTarget(this, callfunc_selector(GameScene::resetGameLayer)),
		NULL);
	fadeLayer->runAction(action);
}
//切换游戏地图
void GameScene::resetGameLayer()
{
	//删除老的GameLayer
	this->removeChildByTag(kGameLayer, true);
	//创建新的GameLayer
	GameLayer *gamelayer = GameLayer::node();
	this->addChild(gamelayer, kGameLayer, kGameLayer);
	//遮罩层执行淡出效果，结束后，调用removeFadeLayer方法删除遮罩层
	CCAction* action = CCSequence::actions(
		CCFadeOut::actionWithDuration(0.5f),
		CCCallFunc::actionWithTarget(this, callfunc_selector(GameScene::removeFadeLayer)),
		NULL);
	this->getChildByTag(kFadeLayer)->runAction(action);
}

void GameScene::removeFadeLayer()
{
	this->removeChildByTag(kFadeLayer, true);
}