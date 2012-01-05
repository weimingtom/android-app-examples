#include "GameLayer.h"

GameLayer::GameLayer(void)
{
	sGlobal->gameLayer = this;
}

GameLayer::~GameLayer(void)
{
	this->unscheduleAllSelectors();
}

bool GameLayer::init()
{
    bool bRet = false;
    do 
    {
		//����tmx��ͼ
		char temp[20];
		sprintf(temp, "%d.tmx", sGlobal->currentLevel);
		map = GameMap::gameMapWithTMXFile(temp);
		addChild(map, kZMap, kZMap);

		//����Hero��ľ�̬��������ʵ��
		hero = Hero::heroWithinLayer();
		//����Hero����ʼλ��
		hero->setPosition(map->positionForTileCoord(sGlobal->heroSpawnTileCoord));
		//��Hero����GameLayer
		addChild(hero, kZHero, kZHero);

		schedule(schedule_selector(GameLayer::update));

        bRet = true;
    } while (0);

    return bRet;
}

void GameLayer::update(ccTime dt)
{
	//�����ʿ��������״̬������Ҫ���³���λ��
	if (hero->isHeroMoving)
	{
		setSceneScrollPosition(hero->getPosition());
	}
}

//������ʿ��ǰλ����Ϣ���������ƶ�����Ӧλ��
void GameLayer::setSceneScrollPosition(CCPoint position)
{
	//��ȡ��Ļ�ߴ�
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	//����Tilemap�Ŀ�ߣ���λ������
	CCSize mapSizeInPixel = CCSizeMake(map->getMapSize().width * map->getTileSize().width, 
		map->getMapSize().height * map->getTileSize().height);
	//ȡ��ʿ��ǰx�������Ļ�е�x�����ֵ�������ʿ��xֵ�ϴ�������
	float x = MAX(position.x, screenSize.width / 2.0f);
	float y = MAX(position.y, screenSize.height / 2.0f);
	//��ͼ�ܿ�ȴ�����Ļ��ȵ�ʱ����п��ܹ���
	if (mapSizeInPixel.width  > screenSize.width)
	{
		//���������������벻�ܳ�����ͼ�ܿ��ȥ��Ļ���1/2
		x = MIN(x, mapSizeInPixel.width - screenSize.width / 2.0f);
	}
	if (mapSizeInPixel.height > screenSize.height)
	{
		y = MIN(y, mapSizeInPixel.height - screenSize.height / 2.0f);
	}
	//��ʿ��ʵ��λ��
	CCPoint heroPosition = ccp(x, y);
	//��Ļ�е�λ��
	CCPoint screenCenter = ccp(screenSize.width/2.0f, screenSize.height/2.0f);
	//������ʿʵ��λ�ú��ص�λ�õľ���
	CCPoint scrollPosition = ccpSub(screenCenter, heroPosition);
	//�������ƶ�����Ӧλ��
	this->setPosition(scrollPosition);
	CCLog("%f,%f", scrollPosition.x, scrollPosition.y);
}

//��ʾ��ʾ��Ϣ
void GameLayer::showTip(const char *tip, CCPoint startPosition)
{
	//�½�һ���ı���ǩ
	CCLabelTTF *tipLabel = CCLabelTTF::labelWithString(tip, "Arial", 20);
	tipLabel->setPosition(ccpAdd(startPosition, ccp(16, 16)));
	this->addChild(tipLabel, kZTip,kZTip);
	//���嶯��Ч��
	CCAction* action = CCSequence::actions(
		CCMoveBy::actionWithDuration(0.5f, ccp(0, 32)),
		CCDelayTime::actionWithDuration(0.5f),
		CCFadeOut::actionWithDuration(0.2f),
		CCCallFuncN::actionWithTarget(this, callfuncN_selector(GameLayer::onShowTipDone)),
		NULL);
	tipLabel->runAction(action);
}

//��ʾ��Ϣ��ʾ���Ļص�
void GameLayer::onShowTipDone(CCNode* pSender)
{
	//ɾ���ı���ǩ
	this->getChildByTag(kZTip)->removeFromParentAndCleanup(true);
}