 #include "HelloWorldScene.h"

using namespace cocos2d;

HelloWorld::HelloWorld()
{

}

HelloWorld::~HelloWorld(void)
{
	for (int i = 0; i < 4; i++)
	{
		//�ͷ�������Ԫ��
		CC_SAFE_RELEASE(walkAnimation[i])
	}
	//�ͷ����鱾��
	CC_SAFE_DELETE_ARRAY(walkAnimation);

	this->unscheduleAllSelectors();
}

CCScene* HelloWorld::scene()
{
    CCScene * scene = NULL;
    do 
    {
        scene = CCScene::node();
        CC_BREAK_IF(! scene);

        HelloWorld *layer = HelloWorld::node();
        CC_BREAK_IF(! layer);

        scene->addChild(layer);
    } while (0);

    return scene;
}

bool HelloWorld::init()
{
    bool bRet = false;
    do 
    {
        CCMenuItemImage *pCloseItem = CCMenuItemImage::itemFromNormalImage(
            "CloseNormal.png",
            "CloseSelected.png",
            this,
            menu_selector(HelloWorld::menuCloseCallback));
        CC_BREAK_IF(! pCloseItem);
        pCloseItem->setPosition(ccp(CCDirector::sharedDirector()->getWinSize().width - 20, 20));
        CCMenu* pMenu = CCMenu::menuWithItems(pCloseItem, NULL);
        pMenu->setPosition(CCPointZero);
        CC_BREAK_IF(! pMenu);
        this->addChild(pMenu, 1);
		
		//����tmx��ͼ
		map = CCTMXTiledMap::tiledMapWithTMXFile("0.tmx");
		addChild(map);

		CCArray * pChildrenArray = map->getChildren();
		CCSpriteBatchNode* child = NULL;
		CCObject* pObject = NULL;
		//����Tilemap������ͼ��
		CCARRAY_FOREACH(pChildrenArray, pObject)
		{
			child = (CCSpriteBatchNode*)pObject;
			if(!child)
				break;
			//��ͼ��������������
			child->getTexture()->setAntiAliasTexParameters();
		}

		walkAnimation = new CCAnimation*[4];
		walkAnimation[kDown] = createAnimationByDirection(kDown);
		walkAnimation[kRight] = createAnimationByDirection(kRight);
		walkAnimation[kLeft] = createAnimationByDirection(kLeft);
		walkAnimation[kUp] = createAnimationByDirection(kUp);

		//��frame0��Ϊ��ʿ�ľ�̬ͼ
		heroSprite = CCSprite::spriteWithSpriteFrame(walkAnimation[kDown]->getFrames()->getObjectAtIndex(0));
		//heroSprite->setPosition(ccp(48, 48));
		heroSprite->setAnchorPoint(CCPointZero);
		heroSprite->setPosition(positionForTileCoord(ccp(1, 11)));
		addChild(heroSprite);
		isHeroWalking = false;

		CCMenuItem *down = CCMenuItemFont::itemFromString("down", this, menu_selector(HelloWorld::menuCallBackMove));
		CCMenuItem *left = CCMenuItemFont::itemFromString("left", this, menu_selector(HelloWorld::menuCallBackMove) );
		CCMenuItem *right = CCMenuItemFont::itemFromString("right", this, menu_selector(HelloWorld::menuCallBackMove) );
		CCMenuItem *up = CCMenuItemFont::itemFromString("up", this, menu_selector(HelloWorld::menuCallBackMove) );
		CCMenu* menu = CCMenu::menuWithItems(down, left, right, up, NULL);
		//Ϊ�˷�����ң���ÿ��menuItem����tag
		down->setTag(kDown);
		left->setTag(kLeft);
		right->setTag(kRight);
		up->setTag(kUp);
		//�˵�����50ˮƽ����
		menu->alignItemsHorizontallyWithPadding(50);
		addChild(menu);

		schedule(schedule_selector(HelloWorld::update));

        bRet = true;
    } while (0);

    return bRet;
}

void HelloWorld::menuCloseCallback(CCObject* pSender)
{
    CCDirector::sharedDirector()->end();
}

CCAnimation* HelloWorld::createAnimationByDirection(HeroDirection direction)
{
	CCTexture2D *heroTexture = CCTextureCache::sharedTextureCache()->addImage("hero.png");
	CCSpriteFrame *frame0, *frame1, *frame2, *frame3;
	//�ڶ���������ʾ��ʾ�����x, y, width, height������direction��ȷ����ʾ��y����
	frame0 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*0, 32*direction, 32, 32));
	frame1 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*1, 32*direction, 32, 32));
	frame2 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*2, 32*direction, 32, 32));
	frame3 = CCSpriteFrame::frameWithTexture(heroTexture, cocos2d::CCRectMake(32*3, 32*direction, 32, 32));
	CCMutableArray<CCSpriteFrame*>* animFrames = new CCMutableArray<CCSpriteFrame*>(4);
	animFrames->addObject(frame0);
	animFrames->addObject(frame1);
	animFrames->addObject(frame2);
	animFrames->addObject(frame3);
	CCAnimation* animation = new CCAnimation();
	//0.2f��ʾÿ֡������ļ��
	animation->initWithFrames(animFrames, 0.07f);
	animFrames->release();

	return animation;
}

void HelloWorld::menuCallBackMove(CCObject* pSender)
{
	if (isHeroWalking)
		return;

    CCNode *node = (CCNode *) pSender;
	//��ť��tag������Ҫ���ߵķ���
	int targetDirection = node->getTag();
	//�ƶ��ľ���
	CCPoint moveByPosition;
	//���ݷ�������ƶ��ľ���
	switch (targetDirection)
	{
	case kDown:
		moveByPosition = ccp(0, -32);
		break;
	case kLeft:
		moveByPosition = ccp(-32, 0);
		break;
	case kRight:
		moveByPosition = ccp(32, 0);
		break;
	case kUp:
		moveByPosition = ccp(0, 32);
		break;
	}
	//����Ŀ�����꣬�õ�ǰ��ʿ��������ƶ�����
	CCPoint targetPosition = ccpAdd(heroSprite->getPosition(), moveByPosition);
	//����checkCollision�����ײ���ͣ������ǽ�ڣ���ֻ��Ҫ������ʿ�ĳ���
	if (checkCollision(targetPosition) == kWall)
	{
		setFaceDirection((HeroDirection)targetDirection);
		return;
	}

	//����CCSpawn�����߶������ƶ�ͬʱִ��
	CCAction *action = CCSequence::actions(
			CCSpawn::actions(
				CCAnimate::actionWithAnimation(walkAnimation[targetDirection], false),
				CCMoveBy::actionWithDuration(0.28f, moveByPosition),
				NULL),
			//�ѷ�����Ϣ���ݸ�onWalkDone����
			CCCallFuncND::actionWithTarget(this, callfuncND_selector(HelloWorld::onWalkDone), (void*)targetDirection),
			NULL);
	heroSprite->runAction(action);
	isHeroWalking = true;
}

void HelloWorld::setFaceDirection(HeroDirection direction)
{
	heroSprite->setTextureRect(CCRectMake(0,32*direction,32,32));
}

void HelloWorld::onWalkDone(CCNode* pTarget, void* data)
{
	//��void*��ת��Ϊint���ٴ�intת����ö������
	int direction = (int) data;
	setFaceDirection((HeroDirection)direction);
	isHeroWalking = false;
	//heroSprite->setPosition(targetPosition);
	setSceneScrollPosition(heroSprite->getPosition());
}

//��cocos2d-x����ת��ΪTilemap����
CCPoint HelloWorld::tileCoordForPosition(CCPoint position)
{
	int x = position.x / map->getTileSize().width;
	int y = (((map->getMapSize().height - 1) * map->getTileSize().height) - position.y) / map->getTileSize().height;
	return ccp(x, y);
}

//��Tilemap����ת��Ϊcocos2d-x����
CCPoint HelloWorld::positionForTileCoord(CCPoint tileCoord)
{
	CCPoint pos =  ccp((tileCoord.x * map->getTileSize().width),
		((map->getMapSize().height - tileCoord.y - 1) * map->getTileSize().height));
	return pos;
}

//������ʿ��ǰλ����Ϣ���������ƶ�����Ӧλ��
void HelloWorld::setSceneScrollPosition(CCPoint position)
{
	//��ȡ��Ļ�ߴ�
	CCSize screenSize = CCDirector::sharedDirector()->getWinSize();
	//ȡ��ʿ��ǰx�������Ļ�е�x�����ֵ�������ʿ��xֵ�ϴ�������
	float x = MAX(position.x, screenSize.width / 2.0f);
	float y = MAX(position.y, screenSize.height / 2.0f);
	//��ͼ�ܿ�ȴ�����Ļ��ȵ�ʱ����п��ܹ���
	if (map->getMapSize().width > screenSize.width)
	{
		//���������������벻�ܳ�����ͼ�ܿ��ȥ��Ļ���1/2
		x = MIN(x, (map->getMapSize().width * map->getTileSize().width) 
			- screenSize.width / 2.0f);
	}
	if (map->getMapSize().height > screenSize.height)
	{
		y = MIN(y, (map->getMapSize().height * map->getTileSize().height) 
			- screenSize.height / 2.0f);
	}
	//��ʿ��ʵ��λ��
	CCPoint heroPosition = ccp(x, y);
	//��Ļ�е�λ��
	CCPoint screenCenter = ccp(screenSize.width/2.0f, screenSize.height/2.0f);
	//������ʿʵ��λ�ú��ص�λ�õľ���
	CCPoint scrollPosition = ccpSub(screenCenter, heroPosition);
	//�������ƶ�����Ӧλ��
	setPosition(scrollPosition);
	CCLog("scene position: %f,%f", scrollPosition.x, scrollPosition.y);
}

void HelloWorld::update(ccTime dt)
{
	//�����ʿ��������״̬������Ҫ���³���λ��
	if (isHeroWalking)
	{
		setSceneScrollPosition(heroSprite->getPosition());
	}
}

//�ж���ײ����
CollisionType HelloWorld::checkCollision(CCPoint heroPosition) 
{
	//cocos2d-x����ת��ΪTilemap����
	CCPoint tileCoord = tileCoordForPosition(heroPosition);
	//�����ʿ���곬����ͼ�߽磬����kWall���ͣ���ֹ���ƶ�
	if (heroPosition.x < 0 || tileCoord.x > map->getMapSize().width - 1 || tileCoord.y < 0 || tileCoord.y > map->getMapSize().height - 1)
		return kWall;
	//��ȡ��ǰ����λ�õ�ͼ��ID
	int tileGid = map->layerNamed("wall")->tileGIDAt(tileCoord);
	//���ͼ��ID��Ϊ0����ʾ��ǽ
	if (tileGid) {
		return kWall;
	}
	//����ͨ��
	return kNone;
}