package dtos;

public class WebhookEvent {
    private long eventId;
    private long subscriptionId;
    private long portalId;
    private long appId;
    private long occurredAt;
    private String subscriptionType;
    private int attemptNumber;
    private long objectId;
    private String changeFlag;
    private String changeSource;
    private String sourceId;

    public WebhookEvent(long eventId, long subscriptionId, long portalId, long appId, long occurredAt,
                        String subscriptionType, int attemptNumber, long objectId, String changeFlag,
                        String changeSource, String sourceId) {
        this.eventId = eventId;
        this.subscriptionId = subscriptionId;
        this.portalId = portalId;
        this.appId = appId;
        this.occurredAt = occurredAt;
        this.subscriptionType = subscriptionType;
        this.attemptNumber = attemptNumber;
        this.objectId = objectId;
        this.changeFlag = changeFlag;
        this.changeSource = changeSource;
        this.sourceId = sourceId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public long getPortalId() {
        return portalId;
    }

    public void setPortalId(long portalId) {
        this.portalId = portalId;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public long getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(long occurredAt) {
        this.occurredAt = occurredAt;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public void setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(String changeFlag) {
        this.changeFlag = changeFlag;
    }

    public String getChangeSource() {
        return changeSource;
    }

    public void setChangeSource(String changeSource) {
        this.changeSource = changeSource;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
